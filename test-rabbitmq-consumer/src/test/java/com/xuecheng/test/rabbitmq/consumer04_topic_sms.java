package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 订阅发布
 * @Author lemerence
 * @Version 1.0
 * @Date 14:36 2019/1/8
 */
public class consumer04_topic_sms {

    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    //交换机
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    //routing_key
    private static final String ROUTINGKEY_SMS="inform.#.sms.#";
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
             * 声明交换机
             * 1.交换机名称
             * 2.交换机类型  fanout、topic、direct、headers
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            /**
                          * 声明队列，如果Rabbit中没有此队列将自动创建
                          * param1:队列名称
                          * param2:是否持久化
                          * param3:队列是否独占此连接
                          * param4:队列不再使用时是否自动删除此队列
                          * param5:队列参数
                          */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            //4)交换机和队列绑定
            /**
              * 参数明细
              * 1、队列名称
              * 2、交换机名称
              * 3、路由key
              */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_SMS);
            //5）接受消息
            //定义消费方法
            DefaultConsumer consumer = new DefaultConsumer(channel){
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
                    long deliveryTag = envelope.getDeliveryTag();
                    String exchange = envelope.getExchange();
                    String message = new String(body,"utf-8");
                    System.out.println(message);
                }
            };
            //监听队列消息
            channel.basicConsume(QUEUE_INFORM_EMAIL,true,consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
