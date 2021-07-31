package com.wangli.gulimall.order.vo;

import com.wangli.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @Author: WangLi
 * @Date: 2021/7/31
 */

@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    /** 错误状态码 **/
    private Integer code;


}
