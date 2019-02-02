package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 20:05 2019/2/1
 */
@Component
public class LoginFilter extends ZuulFilter {
    //路由之前被调用
    @Override
    public String filterType() {
        return "pre";
    }
    //越小越优先调用
    @Override
    public int filterOrder() {
        return 0;
    }
    //是否起作用
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    LoginService loginService;

    //过滤逻辑
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();


        //cookie
        String token = loginService.getTokenFromCookie(request);
        if (StringUtils.isEmpty(token)){
            //拒绝访问
            access_denied(requestContext);
            return null;
        }

        //header
        String authorization = loginService.getAuthorizationFromHeader(request);
        if (StringUtils.isEmpty(authorization)){
            access_denied(requestContext);
            return null;
        }

        //redis
        long expire = loginService.getExpire(token);
        if (expire<=0){
            access_denied(requestContext);
            return null;
        }
        return null;
    }

    /**
     * 拒绝访问
     * @param requestContext
     * @return
     */
    private void access_denied(RequestContext requestContext) {
        HttpServletResponse response = requestContext.getResponse();
        //没有登录
        //拒绝访问
        requestContext.setSendZuulResponse(false);
        //设置状态码
        requestContext.setResponseStatusCode(200);
        //设置响应内容
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String jsonString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(jsonString);
        response.setContentType("application/json;charset=utf-8");
    }
}
