package com.wangli.gulimall.order.dao;

import com.wangli.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:55:12
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
