package com.wangli.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1、整合MyBatis-Plus
 *  *      1）、导入依赖
 *  *      <dependency>
 *  *             <groupId>com.baomidou</groupId>
 *  *             <artifactId>mybatis-plus-boot-starter</artifactId>
 *  *             <version>3.4.3</version>
 *  *      </dependency>
 *  *      2）、配置
 *  *          1、配置数据源；
 *  *              1）、导入数据库的驱动。https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-versions.html
 *  *              2）、在application.yml配置数据源相关信息
 *  *          2、配置MyBatis-Plus；
 *  *              1）、使用@MapperScan配置dao文件文件
 *  *              2）、告诉MyBatis-Plus，sql映射文件位置
 */
@EnableRedisHttpSession
@EnableFeignClients(basePackages = "com.wangli.gulimall.product.feign")
@MapperScan("com.wangli.gulimall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
