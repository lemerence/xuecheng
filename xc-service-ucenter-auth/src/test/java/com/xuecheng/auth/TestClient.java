package com.xuecheng.auth;

import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    //远程请求spring security获取令牌
    @Test
    public void testClient(){

        //url   http://localhost:40400/auth/oauth/token  不能写死
        ServiceInstance instance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = instance.getUri();
        String authUri = uri+"/auth/oauth/token";

        //httpEntity    header,body
        //定义header
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization",getHttpBasic("XcWebApp","XcWebApp"));

        //定义body
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","123");

        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(body,headers);

        //过滤401 400
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode()!=401 && response.getRawStatusCode()!=400){
                    super.handleError(response);
                }
            }
        });

        //httpEntity
        ResponseEntity<Map> entity = restTemplate.exchange(authUri, HttpMethod.POST, httpEntity, Map.class);
        Map body1 = entity.getBody();
        System.out.println(body1);

    }

    //获取httpBasic
    private String getHttpBasic(String clientId,String clientSecret){
        String str = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(str.getBytes());
        return "Basic " + new String(encode);
    }

}
