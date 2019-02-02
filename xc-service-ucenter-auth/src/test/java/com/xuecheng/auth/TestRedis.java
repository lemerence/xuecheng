package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //创建jwt令牌
    @Test
    public void testRedis(){
        //定义key
        String key = "token_key:da118be2-6c39-406c-81fe-7f3ca21d41db";
        //定义value
        Map<String,String> map = new HashMap<>();
        map.put("access_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU0ODk2ODIzNywianRpIjoiZGExMThiZTItNmMzOS00MDZjLTgxZmUtN2YzY2EyMWQ0MWRiIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.U4IkttMMHeOOfZ6lmyM6ckmTSJk-Uak0YjsxsXIQ31Hm6D6goWpb5fFMdcDOhZIbNCK5MB_2DaUdc8gdJ4DcwNRr8UEFKRkdw1JgQpsiEEtzhqZ_0ScQOhqDwErQFNfORWt1Y4FdziQeDBKH33l8wCj7H3O03We67gZNwDxdLnLv9njMdvj0K5imUF7GQ9Kcj9geVDAPMV3GmwUi2VN9qfKGXQ6oBg-d8MP9IGEdFEGC1j60TAtP2cJJ_IogDgtoLsEa4f1ohE5j5H-RhISKwrzZd2JG76WSEVJSe9vgpjvRdkfFNbvFsb08LRkjPD3-BUZCxWL_O9yMbYcXQ1QAgg");
        map.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiJkYTExOGJlMi02YzM5LTQwNmMtODFmZS03ZjNjYTIxZDQxZGIiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU0ODk2ODIzNywianRpIjoiNGY4MjQyNTgtMTU3ZS00Zjc3LTk4MjgtNTE2ODFhZGE1MTg3IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.XdiO6iAsGJf8AofBlorMVV2apOGggzbZ-BArgRQANK56VqOHL9zMMe3IqmRGPrjeLan-CoRpRIZtP0Y-Dh4veVbLWsPc-mK4cUJIvPIuW-EjPWZRTymqdkKIJTOPQEViKB2COJP0iQsep4t3T1G-8P4hskLU-80LdfHz1aOJVL0dTSfEK44jGsWPSM_elCE1X6hVttmrEDPw60kbhy4aBXFmbEhBJDEH_HnOm79Z-PEugYRZexYg6GDtNgTA-AIcspNICPsy6JShHbo4MKwsw86EQayMV6tfmqe0wCLNPN42EpnU3_WFDR8JWUp51YtMVe8kg5UOlxjA8gWCuGjclQ");
        String jsonString = JSON.toJSONString(map);
        //校验key是否存在，如果不存在则返回
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(expire);
        //存储数据
        stringRedisTemplate.boundValueOps(key).set(jsonString,30, TimeUnit.SECONDS);
        //获取数据
        String jwt = stringRedisTemplate.opsForValue().get(key);
        System.out.println(jwt);

    }


}
