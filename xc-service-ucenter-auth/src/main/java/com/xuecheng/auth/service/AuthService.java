package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 22:37 2019/1/31
 */
@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${auth.tokenValiditySeconds}")
    int time;

    /**
     * oauth实现单点登录，申请令牌，保存令牌到redis中
     *
     * @param clientId
     * @param clientSecret
     * @param username
     * @param password
     * @return
     */
    public AuthToken login(String clientId, String clientSecret, String username, String password) {

        //申请令牌
        AuthToken authToken = applyToken(clientId, clientSecret, username, password);
        //保存令牌
        boolean b = saveToken(authToken.getJti(), JSON.toJSONString(authToken), time);
        if (!b) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_SAVETOKEN_FAIL);
        }
        return authToken;
    }

    //申请令牌
    private AuthToken applyToken(String clientId, String clientSecret, String username, String password) {
        //url   http://localhost:40400/auth/oauth/token  不能写死
        ServiceInstance instance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = instance.getUri();
        String authUri = uri + "/auth/oauth/token";

        //httpEntity    header,body
        //定义header
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", getHttpBasic(clientId, clientSecret));

        //定义body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        //过滤401 400
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 401 && response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });

        //httpEntity
        ResponseEntity<Map> entity = restTemplate.exchange(authUri, HttpMethod.POST, httpEntity, Map.class);
        Map tokenMap = entity.getBody();
        if (tokenMap == null
                || tokenMap.get("access_token") == null
                || tokenMap.get("refresh_token") == null
                || tokenMap.get("jti") == null) {
            if (tokenMap != null && tokenMap.get("error_description") != null) {
                String error_description = (String) tokenMap.get("error_description");
                if (error_description.contains("UserDetailsService returned null")) {
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                } else if (error_description.equals("坏的凭证")) {
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }
                ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLUTOKEN_FAIL);
            }
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLUTOKEN_FAIL);
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) tokenMap.get("access_token"));
        authToken.setRefresh_token((String) tokenMap.get("refresh_token"));
        authToken.setJti((String) tokenMap.get("jti"));
        return authToken;
    }

    //保存令牌
    private boolean saveToken(String jti, String content, int time) {
        //保存
        redisTemplate.boundValueOps("user_token:" + jti).set(content, time, TimeUnit.SECONDS);
        //查询
        Long expire = redisTemplate.getExpire("user_token:" + jti, TimeUnit.SECONDS);
        return expire > 0;
    }

    //获取httpBasic
    private String getHttpBasic(String clientId, String clientSecret) {
        String str = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(str.getBytes());
        return "Basic " + new String(encode);
    }

    /**
     * 查询jwt令牌
     * @param tooken
     * @return
     */
    public AuthToken getJwt(String tooken){
        String tokenMap = redisTemplate.opsForValue().get("user_token:"+tooken);
        if (tokenMap!=null){
            AuthToken authToken = null;
            try {
                authToken = JSON.parseObject(tokenMap, AuthToken.class);
            } catch (Exception e) {
                LOGGER.error("getUserToken from redis and execute JSON.parseObject error {}",e.getMessage());
                e.printStackTrace();
            }

            return authToken;
        }
        return null;
    }

    /**
     * 退出，删除reids中的token
     * @param token
     * @return
     */
    public boolean logout(String token){
        Boolean delete = redisTemplate.delete("user_token:"+token);
        return delete;
    }
}
