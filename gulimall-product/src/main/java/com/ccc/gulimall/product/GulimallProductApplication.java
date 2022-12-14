package com.ccc.gulimall.product;

import com.ccc.gulimall.product.dao.CategoryDao;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.ccc.gulimall.product.dao")
@EnableDiscoveryClient()
@EnableFeignClients
public class GulimallProductApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(GulimallProductApplication.class, args);

    }

}
