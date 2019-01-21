package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 14:36 2019/1/8
 */
public class producer01 {

    //队列名称
    private static String QUEUE = "hello_world";

    /**
     * 发送端发布消息
     * @param args
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection conn = null;
        Channel channel = null;
        try {
            //1）创建连接
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
                //创建与RabbitMQ服务的TCP连接
            conn = factory.newConnection();

            //2）创建通道
                //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个回话任务
            channel = conn.createChannel();
            //3）声明队列
            /**
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE,true,false,false,null);
            String message = "helloworld"+System.currentTimeMillis();
            //4）消息发布
            /**
             * 消息发布方法
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             */
            channel.basicPublish("",QUEUE,null,message.getBytes("UTF-8"));
            System.out.println("发布消息：'"+message+"'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel!=null){
                channel.close();
            }
            if (conn!=null){
                conn.close();
            }
        }

    }
}
