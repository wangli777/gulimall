package com.wangli.gulimall.coupon.dao;

import com.wangli.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:49:17
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
