package com.wangli.gulimall.ware.vo;

import lombok.Data;

@Data
public class PurchaseItemDoneVo {
    //{itemId:1,status:4,reason:""}
    /**
     * 采购需求id
     */
    private Long itemId;
    private Integer status;
    private String reason;
}
