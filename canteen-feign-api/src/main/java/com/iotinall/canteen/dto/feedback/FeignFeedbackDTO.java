package com.iotinall.canteen.dto.feedback;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 反馈对象
 *
 * @author loki
 * @date 2021/7/15 19:35
 **/
@Data
public class FeignFeedbackDTO implements Serializable {
    /**
     * 内容
     */
    private String content; // 内容

    /**
     * 处理意见
     */
    private String handleOpinion; // 处理意见

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;
}
