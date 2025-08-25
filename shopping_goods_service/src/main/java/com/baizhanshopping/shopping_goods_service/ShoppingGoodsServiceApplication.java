package com.baizhanshopping.shopping_goods_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
//注册nacos注解
@EnableDiscoveryClient
//开启服务注册发现
@EnableDubbo
//动态刷新配置文件
@RefreshScope
@MapperScan("com.baizhanshopping.shopping_goods_service.mapper")
public class ShoppingGoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingGoodsServiceApplication.class, args);
    }

}
