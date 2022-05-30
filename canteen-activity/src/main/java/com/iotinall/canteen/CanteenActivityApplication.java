package com.iotinall.canteen;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 问卷调查、投票活动启动类
 *
 * @author joelau
 * @date 2021/7/1 11：09
 **/
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoDataSourceProxy
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaAuditing
public class CanteenActivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanteenActivityApplication.class, args);
    }

}
