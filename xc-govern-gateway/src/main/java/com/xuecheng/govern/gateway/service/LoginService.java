package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 20:45 2019/2/1
 */
@Service
public class LoginService {


    /**
     * 从header中取jwt令牌
     * @param request
     * @return
     */
    public String getAuthorizationFromHeader(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
            return null;
        }
        if (!authorization.startsWith("Bearer ")){
            return null;
        }
        return authorization.substring(7);
    }

    /**
     * 从cookie中获取token
     * @param request
     * @return
     */
    public String getTokenFromCookie(HttpServletRequest request){
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if (map==null){
            return null;
        }
        String token = map.get("uid");
        if (StringUtils.isEmpty(token)){
            return null;
        }
        return token;
    }

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 获取redis中，token对应jwt的时间
     * @param token
     * @return
     */
    public long getExpire(String token){
        Long expire = redisTemplate.getExpire("user_token:" + token);
        return expire;
    }

}
