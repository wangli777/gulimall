package com.wangli.gulimall.order.controller;

import com.wangli.gulimall.order.entity.OrderEntity;
import com.wangli.gulimall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangLi
 * @date 2021/7/26 21:22
 * @description
 */
@RestController
public class RabbitTestController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMq")
    public String sendMq(@RequestParam(value = "num", defaultValue = "10") int num) {
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
        return "ok";
    }
}
