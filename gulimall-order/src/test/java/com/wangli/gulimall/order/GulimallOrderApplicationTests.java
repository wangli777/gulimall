package com.wangli.gulimall.order;

import com.wangli.gulimall.order.entity.OrderEntity;
import com.wangli.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GulimallOrderApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试发送消息
     */
    @Test
    public void testSendMessage() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                OrderEntity order = new OrderEntity();
                order.setDeliverySn(String.valueOf(i));
                order.setOrderSn(String.valueOf(i));

                rabbitTemplate.convertAndSend("hello-world-direct-exchange", "hello.world", order);
            }else {
                OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
                reasonEntity.setId(Long.valueOf(i));
                rabbitTemplate.convertAndSend("hello-world-direct-exchange", "hello.world", reasonEntity);
            }

        }
        log.info("消息发送成功");
    }


    /**
     * 创建测试交换机
     */
    @Test
    public void createExchange() {
        DirectExchange directExchange = new DirectExchange("hello-world-direct-exchange");
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功", directExchange.getName());
    }

    /**
     * 创建测试队列
     */
    @Test
    public void createQueue() {
        Queue queue = new Queue("hello-world-direct-queue");
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功", queue.getName());
    }

    /**
     * 绑定上述交换机和队列
     */
    @Test
    public void createBinding() {
        Binding binding = new Binding("hello-world-direct-queue",
                Binding.DestinationType.QUEUE,
                "hello-world-direct-exchange",
                "hello.world",
                null);
        amqpAdmin.declareBinding(binding);
        log.info("Binding[{}->{}->{}]创建成功", binding.getExchange(), binding.getRoutingKey(), binding.getDestination());
    }

}
