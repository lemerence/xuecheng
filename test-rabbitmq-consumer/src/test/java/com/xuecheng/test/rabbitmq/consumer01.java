package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Work queues
 * @Author lemerence
 * @Version 1.0
 * @Date 14:36 2019/1/8
 */
public class consumer01 {

    //队列名称
    private static String QUEUE = "hello_world";

    /**
     * 接收端接受消息
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
            channel.queueDeclare(QUEUE,true,false,false,null);
            //4）监听消息
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                /**
                  * 消费者接收消息调用此方法
                  * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
                  * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
                  * @param properties
                  * @param body
                  * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String exchange = envelope.getExchange();//交换机
                    String routingKey = envelope.getRoutingKey();//路由key
                    long deliveryTag = envelope.getDeliveryTag();//消息id
                    String msg = new String(body, "utf-8");
                    System.out.println("接受到消息：'"+msg+"'");
                }
            };
            /**
              * 监听队列String queue, boolean autoAck,Consumer callback
              * 参数明细
              * 1、队列名称
              * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
              * 3、消费消息的方法，消费者接收到消息后调用此方法
              */
            channel.basicConsume(QUEUE,true,consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
