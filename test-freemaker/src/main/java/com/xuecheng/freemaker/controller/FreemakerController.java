package com.xuecheng.freemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 页面静态化
 * @Author lemerence
 * @Version 1.0
 * @Date 16:27 2019/1/2
 */
@Controller
@RequestMapping("/freemarker")
public class FreemakerController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/banner")
    public String index_banner(Map<String, Object> map){
        String dataUrl = "http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = entity.getBody();
        map.put("model",body);
        return "index_banner";
    }

    @RequestMapping("/course")
    public String course(Map<String, Object> map){
        String dataUrl = "http://localhost:31200/course/courseView/4028e581617f945f01617f9dabc40000";
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = entity.getBody();
        map.putAll(body);
        return "course";
    }
}
