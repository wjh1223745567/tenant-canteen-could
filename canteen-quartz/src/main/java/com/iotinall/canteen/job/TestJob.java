package com.iotinall.canteen.job;

import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.service.FeignFromQuartzDishService;
import com.iotinall.canteen.service.QuartzAuthService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestJob implements Job {

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("开始执行测试任务！！！！");
        FeignFromQuartzDishService feignFromQuartzDishService = SpringContextUtil.getBean(FeignFromQuartzDishService.class);
        feignFromQuartzDishService.findById("2213", QuartzAuthService.token);
    }
}
