package com.wangli.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author WangLi
 * @date 2021/6/3 23:46
 * @description
 */
@Configuration
public class GuliMallCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        //跨域配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许哪些请求头进行跨域
        corsConfiguration.addAllowedHeader("*");
        //允许哪些请求来源进行跨域
        corsConfiguration.addAllowedOrigin("*");
        //允许哪些请求方法进行跨域
        corsConfiguration.addAllowedMethod("*");
        //是否允许携带cookie进行跨域
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        //注册跨域配置，“/**”表示给任意路径都进行跨域配置
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        CorsWebFilter corsWebFilter = new CorsWebFilter(configurationSource);
        return corsWebFilter;
    }

}
