package com.iotinall.canteen.common.thread;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.iotinall.canteen.common.entity.OperationLog;
import com.iotinall.canteen.common.repository.OperationLogRepository;
import com.iotinall.canteen.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 添加操作日志
 */
@Slf4j
public class OperationLogThread implements Runnable{

    private final OperationLog operationLog;

    public OperationLogThread(OperationLog operationLog) {
        this.operationLog = operationLog;
    }

    @Override
    public void run() {
        DynamicDataSourceContextHolder.push("storehouse");
        OperationLogRepository operationLogRepository = SpringContextUtil.getBean(OperationLogRepository.class);
        operationLogRepository.save(operationLog);
    }
}
