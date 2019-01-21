package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 11:12 2019/1/11
 */
@Configuration
public class RabbitmqConfig {
    //队列名称Bean
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";

    //交换机名称Bean
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    //配置文件中读取队列名称
    @Value("${xuecheng.mq.queue}")
    public  String queue_cms_postpage_name;
    //配置文件中读取routingKey
    @Value("${xuecheng.mq.routingKey}")
    public String routingKey;

    //声明队列
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue QUEUE_CMS_POSTPAGE(){
        return new Queue(queue_cms_postpage_name);
    }

    //声明交换机
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    //绑定交换机与队列
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_CMS_POSTPAGE)Queue queue,@Qualifier(EX_ROUTING_CMS_POSTPAGE)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

}
