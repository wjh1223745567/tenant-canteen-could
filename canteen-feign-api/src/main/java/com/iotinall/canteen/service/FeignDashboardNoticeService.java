package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.feedback.FeignFeedbackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 反馈
 */
@FeignClient(value = "canteen-menu", contextId = "canteen-dashboard-notice")
public interface FeignDashboardNoticeService {

    @GetMapping(value = "/dashboardNotice/feign/getDashboardNoticeList")
    List<FeignFeedbackDTO> getDashboardNoticeList();
}
