package com.wangli.gulimall.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WangLi
 * @date 2021/8/8 14:55
 * @description 容器中的Queue、Exchange、Binding 会自动创建（在RabbitMQ）不存在的情况下
 */
@Configuration
public class MyMQConfig {

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        /*
            Queue(String name,  队列名字
            boolean durable,  是否持久化
            boolean exclusive,  是否排他
            boolean autoDelete, 是否自动删除
            Map<String, Object> arguments) 属性
         */
        //String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        // 消息过期时间 30s
        arguments.put("x-message-ttl", 30000);
        return new Queue("order.delay.queue", true, false, false, arguments);
    }

    @Bean
    public Queue orderReleaseOrderQueue() {
        //String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        return new Queue("order.release.order.queue", true, false, false);
    }

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    @Bean
    public Binding orderCreateBinding() {
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.create.order", null);
    }

    @Bean
    public Binding orderReleaseBinding() {
        return new Binding("order.release.order.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.release.order", null);
    }

}
