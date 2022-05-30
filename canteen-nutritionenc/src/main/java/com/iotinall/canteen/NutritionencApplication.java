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

/**
 * 食谱百科，字典表，单库
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoDataSourceProxy
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = JpaRepositoryEnhanceImpl.class)
public class NutritionencApplication {
    public static void main(String[] args) {
        SpringApplication.run(NutritionencApplication.class, args);
    }
}
