package com.wangli.gulimall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author WangLi
 * @date 2021/7/15 22:18
 * @description 自定义线程池
 */
@Configuration
//@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
// 如果ThreadPoolConfigProperties已经放入容器中了，可以不用再指定，直接使用
public class MyThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties configProperties) {
        return new ThreadPoolExecutor(
                configProperties.getCoreSize(),
                configProperties.getMaxSize(),
                configProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
