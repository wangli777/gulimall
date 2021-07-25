package com.wangli.gulimall.cart.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.wangli.common.constant.CartConstant;
import com.wangli.common.utils.R;
import com.wangli.gulimall.cart.feign.ProductFeignService;
import com.wangli.gulimall.cart.interceptor.CartInterceptor;
import com.wangli.gulimall.cart.service.CartService;
import com.wangli.gulimall.cart.to.UserInfoTo;
import com.wangli.gulimall.cart.vo.CartItemVo;
import com.wangli.gulimall.cart.vo.CartVo;
import com.wangli.gulimall.cart.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author WangLi
 * @date 2021/7/24 22:17
 * @description
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;


    /**
     * 添加商品到购物车
     *
     * @param skuId
     * @param num
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        //1、获取当前用户的redis购物车
        BoundHashOperations<String, Object, Object> cartOps = this.getCartOps();
        //判断redis中是否有该商品
        String productRedisValue = (String) cartOps.get(skuId.toString());
        if (StringUtils.isBlank(productRedisValue)) {
            //如果没有就添加数据
            //2、添加商品到购物车
            CartItemVo cartItemVo = new CartItemVo();
            //2.1、异步 远程调用查询sku基本信息
            CompletableFuture<Void> getSkuInfoFuture = CompletableFuture.runAsync(() -> {
                R data = productFeignService.getSkuInfo(skuId);
                SkuInfoVo skuInfo = data.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItemVo.setCount(num);
                cartItemVo.setImage(skuInfo.getSkuDefaultImg());
                cartItemVo.setPrice(skuInfo.getPrice());
                cartItemVo.setSkuId(skuInfo.getSkuId());
                cartItemVo.setTitle(skuInfo.getSkuTitle());
            }, executor);

            //2.2、异步 远程调用查询sku销售属性
            CompletableFuture<Void> getSkuAttrFuture = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItemVo.setSkuAttrValues(skuSaleAttrValues);
            }, executor);

            //3、等待所有异步任务完成
            CompletableFuture.allOf(getSkuInfoFuture, getSkuAttrFuture).get();

            //4、将购物车项转JSON，存入redis
            String jsonStr = JSONUtil.toJsonStr(cartItemVo);
            cartOps.put(skuId.toString(), jsonStr);

            return cartItemVo;

        } else {
            //购物车中已有此商品，修改商品数量
            CartItemVo cartItemVo = JSONUtil.toBean(productRedisValue, CartItemVo.class);
            cartItemVo.setCount(cartItemVo.getCount() + num);

            String jsonStr = JSONUtil.toJsonStr(cartItemVo);
            cartOps.put(skuId.toString(), jsonStr);

            return cartItemVo;
        }
    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String result = (String) cartOps.get(skuId.toString());
        return JSONUtil.toBean(result, CartItemVo.class);
    }

    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {

        CartVo cartVo = new CartVo();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null) {
            //1、登录
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            //看临时购物车是否有数据
            String temptCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            //2、如果临时购物车的数据还未进行合并
            List<CartItemVo> tempCartItems = getCartItems(temptCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据需要进行合并操作
                for (CartItemVo item : tempCartItems) {
                    addToCart(item.getSkuId(), item.getCount());
                }
                //清除临时购物车的数据
                clearCartInfo(temptCartKey);
            }

            //3、获取登录后的购物车数据【包含合并过来的临时购物车的数据和登录后购物车的数据】
            List<CartItemVo> cartItems = getCartItems(cartKey);
            cartVo.setItems(cartItems);

        } else {
            //没登录
            String cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车里面的所有购物项
            List<CartItemVo> cartItems = getCartItems(cartKey);
            cartVo.setItems(cartItems);
        }

        return cartVo;
    }

    @Override
    public void clearCartInfo(String cartKey) {
        stringRedisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        //查询购物车里面的商品
        CartItemVo cartItem = getCartItem(skuId);
        //修改商品状态
        cartItem.setCheck(checked == 1);
        //序列化存入redis中
        String redisValue = JSONUtil.toJsonStr(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), redisValue);
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        //查询购物车里面的商品
        CartItemVo cartItem = getCartItem(skuId);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        if (num == 0) {
            //删除
            cartOps.delete(skuId.toString());
        }else{
            cartItem.setCount(num);
            //序列化存入redis中
            String redisValue = JSONUtil.toJsonStr(cartItem);
            cartOps.put(skuId.toString(),redisValue);
        }
    }

    @Override
    public void deleteIdCartInfo(Integer skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    private List<CartItemVo> getCartItems(String cartKey) {
        //获取购物车里面的所有商品
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (CollUtil.isNotEmpty(values)) {
            return values.stream().map((obj) -> {
                String str = (String) obj;
                return JSONUtil.toBean(str, CartItemVo.class);
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 获取当前用户的redis购物车
     *
     * @return BoundHashOperations
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Long userId = userInfoTo.getUserId();
        String cartKey = "";
        if (userId != null) {
            cartKey = CartConstant.CART_PREFIX + userId;
        } else {
            cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
        }

        return stringRedisTemplate.boundHashOps(cartKey);
    }
}
