package com.iotinall.canteen.gateway.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 心跳机制
 */
@Slf4j
@Configuration
@EnableScheduling
public class HeartbeatConfiguration {

    /**
     * 心跳
     */
    public static final String heartbeatkey = "canteen-heartbeat:";

    @Resource
    public RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.application.name}")
    private String sysName;

    @Scheduled(fixedDelay = 9 * 1000)
    public void execute() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(heartbeatkey + sysName, 0, 10, TimeUnit.SECONDS);
    }


}
