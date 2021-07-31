package com.wangli.gulimall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: 锁定库存的vo
 * @Author: WangLi
 * @Date: 2021/7/31
 */

@Data
public class WareSkuLockVo {

    private String orderSn;

    /** 需要锁住的所有库存信息 **/
    private List<OrderItemVo> locks;



}
