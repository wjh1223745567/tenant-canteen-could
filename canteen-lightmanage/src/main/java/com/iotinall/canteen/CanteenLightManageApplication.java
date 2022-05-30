package com.iotinall.canteen;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhanceImpl;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 明厨亮灶，光盘行动服务启动类
 *
 * @author loki
 * @date 2021/6/8 15:00
 **/
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoDataSourceProxy
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = JpaRepositoryEnhanceImpl.class)
public class CanteenLightManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanteenLightManageApplication.class, args);
    }

}
