package com.iotinall.canteen.service.impl;

import com.iotinall.canteen.common.configuration.HeartbeatConfiguration;
import com.iotinall.canteen.util.MailSendUtils;
import com.iotinall.canteen.repository.ServerMonitoringRecordRepository;
import com.iotinall.canteen.service.ServerMonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@Primary
@RefreshScope
public class ServerMonitoringServiceImpl implements ServerMonitoringService {

    @Resource
    private ServerMonitoringRecordRepository serverMonitoringRecordRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.mail.toUser}")
    private String toUser;

    private static final Set<String> hisSys = Collections.synchronizedSet(new HashSet<>());

    @Override
    public synchronized void executeServerMonitoring() {
        Set<String> stringSet = redisTemplate.keys(HeartbeatConfiguration.heartbeatkey + "*");
        if (stringSet != null) {
            Set<String> upLine = new HashSet<>();
            for (String nowSys : stringSet) {
                if (!hisSys.contains(nowSys)) {
                    log.info("服务器上线：{}", nowSys.replace(HeartbeatConfiguration.heartbeatkey, ""));
                    upLine.add(nowSys);
                    //第一次启动不发送邮件
                    if(StringUtils.isNotBlank(toUser) && !hisSys.isEmpty()){
                        try {
                            MailSendUtils.sendSimpleMail(toUser, "服务器监听", "服务器上线：" + nowSys.replace(HeartbeatConfiguration.heartbeatkey, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            hisSys.addAll(upLine);

            Set<String> downLine = null;
                downLine = new HashSet<>();
                for (String hisSy : hisSys) {
                    if (!stringSet.contains(hisSy)) {
                        log.info("服务器下线：{}", hisSy.replace(HeartbeatConfiguration.heartbeatkey, ""));
                        downLine.add(hisSy);
                        if(StringUtils.isNotBlank(toUser)) {
                            try {
                                MailSendUtils.sendSimpleMail(toUser, "服务器监听", "服务器下线：" + hisSy.replace(HeartbeatConfiguration.heartbeatkey, ""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            hisSys.removeAll(downLine);
        }
    }
}
