package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式：没有路由key的限制所有队列都能监听
 * @Author lemerence
 * @Version 1.0
 * @Date 14:36 2019/1/8
 */
public class producer02_publish {

    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //交换机
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";
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
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            /**
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            //4)交换机和队列绑定
            /**
              * 参数明细
              * 1、队列名称
              * 2、交换机名称
              * 3、路由key
              */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");
            //5）消息发布
            for (int i = 0; i < 10; i++) {
                String message = "inform to user"+i;
                //向交换机发送消息
                /**
                  * 参数明细
                  * String exchange, String routingKey, BasicProperties props,byte[] body
                  * 1、交换机名称，不指令使用默认交换机名称 Default Exchange
                  * 2、routingKey（路由key），根据key名称将消息转发到具体的队列，这里填写队列名称表示消息将发到此队列
                  * 3、消息属性
                  * 4、消息内容
                  */
                channel.basicPublish(EXCHANGE_FANOUT_INFORM,"",null,message.getBytes());
                System.out.println("发送消息:'"+message+"'");
            }
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
