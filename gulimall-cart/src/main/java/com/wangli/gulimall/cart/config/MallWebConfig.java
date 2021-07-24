package com.wangli.gulimall.cart.config;

import com.wangli.gulimall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WangLi
 * @date 2021/7/24 16:54
 * @description
 */
@Configuration
public class MallWebConfig implements WebMvcConfigurer {
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/cartList.html").setViewName("cartList");
//        registry.addViewController("/success.html").setViewName("success");
//    }

    /**
     * 设置一个拦截器，拦截所有 /** 请求
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
