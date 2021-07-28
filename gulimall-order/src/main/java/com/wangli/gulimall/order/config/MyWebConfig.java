package com.wangli.gulimall.order.config;

import com.wangli.gulimall.order.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WangLi
 * @date 2021/7/27 22:01
 * @description web配置
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加一个LoginInterceptor登录拦截器，拦截当前系统所有的资源路径
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
