package com.iotinall.canteen.configuration;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {

    @Bean
    public Scheduler scheduler(Scheduler scheduler) throws SchedulerException {
        scheduler.getListenerManager().addJobListener(new SimpleJobListener());
        return scheduler;
    }

}
