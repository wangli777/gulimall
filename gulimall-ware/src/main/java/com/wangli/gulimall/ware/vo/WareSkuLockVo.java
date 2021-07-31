package com.wangli.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: WangLi
 * @Date: 2021/7/31
 */
@Data
public class WareSkuLockVo {

    private String orderSn;

    /** 需要锁住的所有库存信息 **/
    private List<OrderItemVo> locks;

}
