package com.wangli.gulimall.order.to;

import com.wangli.gulimall.order.entity.OrderEntity;
import com.wangli.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author WangLi
 * @date 2021/7/31 16:41
 * @description
 */
@Data
public class OrderCreateTo {

    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;

    /** 运费 **/
    private BigDecimal fare;

}
