package com.wangli.gulimall.cart.service;

import com.wangli.gulimall.cart.vo.CartItemVo;
import com.wangli.gulimall.cart.vo.CartVo;

import java.util.concurrent.ExecutionException;

/**
 * @author WangLi
 * @date 2021/7/24 22:17
 * @description
 */
public interface CartService {
    CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItemVo getCartItem(Long skuId);

    CartVo getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车的数据
     * @param cartKey
     */
    void clearCartInfo(String cartKey);

    void checkItem(Long skuId, Integer checked);

    void changeItemCount(Long skuId, Integer num);

    void deleteIdCartInfo(Integer skuId);
}
