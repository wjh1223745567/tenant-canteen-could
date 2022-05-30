package com.iotinall.canteen.service;

import com.iotinall.canteen.repository.StockFlwTaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 流程任务逻辑处理类
 *
 * @author loki
 * @date 2021/6/4 11:33
 **/
@Service
public class StockFlwTaskService {
    @Resource
    private StockFlwTaskRepository stockFlwTaskRepository;

}
