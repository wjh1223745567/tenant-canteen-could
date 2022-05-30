package com.iotinall.canteen.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CanteenGatewayApplication {
    public static void main(String[] args){
        SpringApplication.run(CanteenGatewayApplication.class, args);
    }
}
