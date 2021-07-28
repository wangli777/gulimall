package com.wangli.gulimall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author WangLi
 * @date 2021/7/28 22:53
 * @description Feign远程调用配置
 */
@Configuration
public class MyFeignConfig {

    /**
     * Feign拦截器
     * 拦截feign请求，并给feign远程调用请求的请求头中添加上原请求的请求头信息
     * @return
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {

        return template ->{
            //1、使用RequestContextHolder拿到刚进来的请求数据
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                //2、同步请求头的数据（主要是cookie）
                HttpServletRequest request = requestAttributes.getRequest();
                String cookie = request.getHeader("Cookie");
                template.header("Cookie", cookie);
            }
        };
//        return new RequestInterceptor() {
//            @Override
//            public void apply(RequestTemplate template) {
//                //1、使用RequestContextHolder拿到刚进来的请求数据
//                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//                if (requestAttributes != null) {
//                    //2、同步请求头的数据（主要是cookie）
//                    HttpServletRequest request = requestAttributes.getRequest();
//                    String cookie = request.getHeader("Cookie");
//                    template.header("Cookie", cookie);
//                }
//            }
//        };

    }
}
