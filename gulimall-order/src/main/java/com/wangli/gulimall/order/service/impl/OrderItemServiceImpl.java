package com.wangli.gulimall.order.service.impl;

import com.rabbitmq.client.Channel;
import com.wangli.gulimall.order.entity.OrderEntity;
import com.wangli.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangli.common.utils.PageUtils;
import com.wangli.common.utils.Query;

import com.wangli.gulimall.order.dao.OrderItemDao;
import com.wangli.gulimall.order.entity.OrderItemEntity;
import com.wangli.gulimall.order.service.OrderItemService;

/**
 * @RabbitListener:一般标在类上，指定监听的队列
 * @RabbitHandler:一般标在方法上，可以以重载的方式接受同一个队列中的不同类型消息
 */
@RabbitListener(queues = {"hello-world-direct-queue"})
@Service("orderItemService")
@Slf4j
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    @RabbitHandler
    public void testListen(OrderEntity order, Message message, Channel channel) {
        log.info("监听到消息[{}]，类型[{}]", order, order.getClass());
    }

    @RabbitHandler
    public void testListen(OrderReturnReasonEntity reasonEntity) {
        log.info("监听到消息[{}]，类型[{}]", reasonEntity, reasonEntity.getClass());
    }

}