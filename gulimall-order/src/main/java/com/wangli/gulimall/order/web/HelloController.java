package com.wangli.gulimall.order.web;

import com.wangli.gulimall.order.entity.OrderEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

/**
 * @author WangLi
 * @date 2021/7/27 20:55
 * @description
 */
@Controller
public class HelloController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/test/createOrder")
    @ResponseBody
    public String createOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        orderEntity.setCreateTime(new Date());

        //给死信队列发送订单消息
        rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", orderEntity);

        return "ok";
    }

    @GetMapping("/{page}.html")
    public String goPage(@PathVariable("page") String page) {
        return page;
    }


}
