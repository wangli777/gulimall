package com.wangli.gulimall.cart.controller;

import com.wangli.gulimall.cart.interceptor.CartInterceptor;
import com.wangli.gulimall.cart.to.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author WangLi
 * @date 2021/7/24 22:21
 * @description
 */
@Controller
@Slf4j
public class CartController {

    @GetMapping("/cartList.html")
    public String cartListPage() {
        //从ThreadLocal中获取拦截器封装好的用户信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        log.debug("cartListPage threadLocal data:{}",userInfoTo);

        return "cartList";
    }
}
