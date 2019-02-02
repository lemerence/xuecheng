package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 20:05 2019/2/1
 */
//@Component
public class LoginFilterTest extends ZuulFilter {
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
    //过滤逻辑
    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
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
            return null;
        }

        return null;
    }
}
