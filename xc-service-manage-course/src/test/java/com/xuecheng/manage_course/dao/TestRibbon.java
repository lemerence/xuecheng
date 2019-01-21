package com.xuecheng.manage_course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 21:54 2019/1/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void test(){
        for (int i = 0; i < 10; i++) {


            String serverId = "XC-SERVICE-MANAGE-CMS";
            ResponseEntity<Map> entity = restTemplate.getForEntity("http://" + serverId + "/cms/page/get/5a7be667d019f14d90a1fb1c", Map.class);
            Map body = entity.getBody();
            System.out.println(body);
        }
    }


}
