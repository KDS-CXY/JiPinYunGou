package com.baizhanshopping.shopping_manager_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

//因为引入了common的依赖但是这个不需要数据库操作所以不要配置数据源
@EnableDiscoveryClient
@RefreshScope
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ShoppingManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingManagerApiApplication.class, args);
    }

}
