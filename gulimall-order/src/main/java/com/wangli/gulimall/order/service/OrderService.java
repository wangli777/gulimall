package com.wangli.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangli.common.utils.PageUtils;
import com.wangli.gulimall.order.entity.OrderEntity;
import com.wangli.gulimall.order.vo.OrderConfirmVo;
import com.wangli.gulimall.order.vo.OrderSubmitVo;
import com.wangli.gulimall.order.vo.SubmitOrderResponseVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:55:12
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);
}

