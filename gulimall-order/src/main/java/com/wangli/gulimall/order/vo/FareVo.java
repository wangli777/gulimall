package com.wangli.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 运费
 * @Author: WangLi
 * @Date: 2021/7/31
 */

@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
