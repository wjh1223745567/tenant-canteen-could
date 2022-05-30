package com.iotinall.canteen.netty.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动类
 */

@Slf4j
@Service
@ConditionalOnProperty(value = "netty.open", havingValue = "true")
public class InitStartNetty implements ApplicationRunner {
    @Resource
    private StartNettyThread startNettyThread;

    @Value("${netty.http.connection}")
    private Integer isKeepHttpConnection;
    @Value("${netty.http.port}")
    private Integer isKeepHttpConnectionPort;
    @Value("${netty.start.interval}")
    private int iTimingStartInterval;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        try {
            if (Objects.nonNull(isKeepHttpConnection) && Objects.nonNull(isKeepHttpConnectionPort)) {
                if (0 < isKeepHttpConnection) {
                    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                            new BasicThreadFactory.Builder().namingPattern("keep-http-connection-thread").daemon(true).build());
                    startNettyThread.setIsKeepHttpConnectionPort(isKeepHttpConnectionPort);
                    scheduledExecutorService.scheduleWithFixedDelay(startNettyThread, 0, iTimingStartInterval, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
