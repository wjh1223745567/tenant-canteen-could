package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.feedback.FeignFeedbackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 反馈
 */
@FeignClient(value = "canteen-menu", contextId = "canteen-feedback")
public interface FeignFeedbackService {

    @GetMapping(value = "/mess/feedbacks/feign/getFeedbackList")
    List<FeignFeedbackDTO> getFeedbackList();
}
