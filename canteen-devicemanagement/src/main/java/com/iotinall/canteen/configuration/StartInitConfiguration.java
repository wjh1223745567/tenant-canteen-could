package com.iotinall.canteen.configuration;

import com.iotinall.canteen.service.HikVisionDeviceService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class StartInitConfiguration implements ApplicationRunner {

    @Resource
    private HikVisionDeviceService hikVisionDeviceService;

    @Override
    public void run(ApplicationArguments args) {
        hikVisionDeviceService.subscribeToEvents();
    }
}
