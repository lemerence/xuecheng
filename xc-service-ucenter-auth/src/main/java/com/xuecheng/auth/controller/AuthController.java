package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 18:05 2019/1/31
 */
@RestController
public class AuthController implements AuthControllerApi {

    @Autowired
    AuthService authService;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Override
    @PostMapping("/login")
    public LoginResult login(LoginRequest loginRequest) {

        if (loginRequest==null|| StringUtils.isEmpty(loginRequest.getUsername())){
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (StringUtils.isEmpty(loginRequest.getPassword())){
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        //登录
        AuthToken authToken = authService.login(clientId, clientSecret, username, password);
        //保存令牌到cookie
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",authToken.getJti(),cookieMaxAge,false);
        return new LoginResult(CommonCode.SUCCESS,authToken.getJti());
    }

    @Override
    @GetMapping("/logout")
    public ResponseResult logout(HttpServletRequest request,HttpServletResponse response) {
        //从cookie中获取身份令牌
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        String token = map.get("uid");

        //从redis中删除令牌
        boolean logout = authService.logout(token);
        if (!logout){
            return new ResponseResult(CommonCode.FAIL);
        }

        //删除cookie中的token
        CookieUtil.addCookie(response,cookieDomain,"/","uid",token,0,false);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("/getJwt")
    public JwtResult getJwt(HttpServletRequest request){
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        String uid = map.get("uid");
        AuthToken authToken = authService.getJwt(uid);
        if (authToken==null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        return new JwtResult(CommonCode.SUCCESS,authToken.getAccess_token());
    }


}
