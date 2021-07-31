package com.wangli.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 无库存抛出的异常
 * @Author: WangLi
 * @Date: 2021/7/31
 */

public class NoStockException extends RuntimeException {

    @Getter @Setter
    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品id："+ skuId + "库存不足！");
    }

    public NoStockException(String msg) {
        super(msg);
    }


}
