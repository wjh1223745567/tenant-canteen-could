package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.FeignEmployeeService;
import com.iotinall.canteen.service.FeignFeedbackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后厨模块统计
 *
 * @author loki
 * @date 2021/7/13 16:27
 **/
@RestController
@RequestMapping("statistics/feedback")
public class FeedbackStatisticsController {
    @Resource
    private FeignFeedbackService feignFeedbackService;

    @GetMapping(value = "feedback-list")
    public ResultDTO getFeedbackList() {
        return ResultDTO.success(feignFeedbackService.getFeedbackList());
    }
}
