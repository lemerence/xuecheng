package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 页面发布方配置交换机信息
 * @Author lemerence
 * @Version 1.0
 * @Date 15:14 2019/1/12
 */
@Configuration
public class RabbitmqConfig {
    //交换机名称
    public static final String EX_ROUTING_CMS_POSTAGE="ex_routing_cms_postpage";
    @Bean(EX_ROUTING_CMS_POSTAGE)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTAGE).durable(true).build();
    }
}
