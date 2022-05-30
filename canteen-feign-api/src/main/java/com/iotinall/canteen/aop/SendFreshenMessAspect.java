package com.iotinall.canteen.aop;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.iotinall.canteen.annotations.FreshenMess;
import com.iotinall.canteen.service.FeignTcpClientService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 发送通知消息
 */
@Slf4j
@Aspect
@Component
public class SendFreshenMessAspect {

    @Resource
    private FeignTcpClientService feignTcpClientService;

    @After("@annotation(freshenMess)")
    public void toMess(JoinPoint joinPoint, FreshenMess freshenMess){
        if(freshenMess != null){
            String dataSource = DynamicDataSourceContextHolder.peek();
            feignTcpClientService.sendFreshenMess(dataSource, Arrays.stream(freshenMess.value()).collect(Collectors.toSet()));
        }
    }
}
