package com.baizhanshopping.shopping_category_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EnableDiscoveryClient
@EnableDubbo
@RefreshScope
@MapperScan("com.baizhanshopping.shopping_category_service.mapper")
@SpringBootApplication
public class ShoppingCategoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCategoryServiceApplication.class, args);
    }

}
