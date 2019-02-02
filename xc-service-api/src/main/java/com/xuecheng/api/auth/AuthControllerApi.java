package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 18:00 2019/1/31
 */
@Api(value = "用户认证",description = "用户认真接口")
public interface AuthControllerApi {

    @ApiOperation("登陆")
    public LoginResult login(LoginRequest loginRequest);

    @ApiOperation("退出")
    public ResponseResult logout(HttpServletRequest request, HttpServletResponse response);

    @ApiOperation("查询用户信息")
    public JwtResult getJwt(HttpServletRequest request);
}
