package com.wangli.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author WangLi
 * @date 2021/7/29 22:10
 * @description
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
