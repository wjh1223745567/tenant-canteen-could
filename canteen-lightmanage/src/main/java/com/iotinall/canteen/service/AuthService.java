package com.iotinall.canteen.service;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUserDetails;
import com.iotinall.canteen.dto.organization.FeignLoginReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Service
@RefreshScope
public class AuthService {

    @Resource
    private FeignFromQuartzOrgService feignFromQuartzOrgService;

    @Value("${authuser.username:null}")
    private String username;

    @Value("${authuser.pwd:null}")
    private String pwd;

    @Value("${login-timeout}")
    private Integer loginTimeout;

    /**
     * 鉴权token
     */
    public static String token;

    public static LocalDateTime lastUpdateTime;

    @Scheduled(fixedDelay = 120 * 1000)
    public void getToken(){
        LocalDateTime now = LocalDateTime.now();
        if(lastUpdateTime == null || Duration.between(lastUpdateTime, now).toMinutes() >= loginTimeout - 1){
            FeignLoginReq feignLoginReq = new FeignLoginReq()
                    .setUsername(username)
                    .setPwd(pwd);
            ResultDTO<SecurityUserDetails> resultDTO = feignFromQuartzOrgService.login(feignLoginReq);
            if(Objects.equals(resultDTO.getCode(), "0")){
                token = resultDTO.getData().getToken();
                lastUpdateTime = now;
                log.info("获取到新token：{}, {}", token, lastUpdateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }else{
                log.error("登录鉴权失败：{}", resultDTO.getMsg());
            }
        }
    }

}
