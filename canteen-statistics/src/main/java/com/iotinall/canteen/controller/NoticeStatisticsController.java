package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.FeignDashboardNoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 看板消息通知
 *
 * @author loki
 * @date 2021/7/16 9:41
 **/
@RestController
@RequestMapping("statistics/notice")
public class NoticeStatisticsController {
    @Resource
    private FeignDashboardNoticeService feignDashboardNoticeService;

    /**
     * 大屏，消息通知
     *
     * @author loki
     * @date 2021/7/16 9:44
     **/
    @GetMapping
    public ResultDTO getNoticeList() {
        return ResultDTO.success(feignDashboardNoticeService.getDashboardNoticeList());
    }
}
