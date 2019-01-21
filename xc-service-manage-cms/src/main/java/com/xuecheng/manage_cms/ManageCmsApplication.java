package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 启动类
 * @Author: lemerence
 * @Date: Create in 14:20 2018/12/25
 */
@EnableDiscoveryClient//发现注册
//未能加载嵌入的供web应用加载的空间，是因为缺少ServletWebServerFactory bean两种方法解决
@SpringBootApplication
//@EnableAutoConfiguration
@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"})//扫描接口   basePackages别名AliasFor("value")
@ComponentScan(basePackages = {"com.xuecheng.framework.exception"})//扫描本项目下所有类
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"})//扫描本项目下所有类
public class ManageCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
