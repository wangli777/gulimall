package com.wangli.gulimall.order.listener;

import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wangli.gulimall.order.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author WangLi
 * @date 2021/8/8 15:14
 * @description
 */
@RabbitListener(queues = "order.release.order.queue")
@Service
@Slf4j
public class OrderCloseListener {

    @RabbitHandler
    public void listen(OrderEntity order, Channel channel, Message message) throws IOException {
        log.debug("收到过期的订单信息，准备关闭订单:{},{}", order.getOrderSn(), DateUtil.formatDateTime(order.getCreateTime()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
