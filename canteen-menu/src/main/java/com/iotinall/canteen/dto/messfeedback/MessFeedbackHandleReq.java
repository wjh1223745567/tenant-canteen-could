package com.iotinall.canteen.dto.messfeedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author xin-bing
 * @date 10/26/2019 19:38
 */
@Data
@ApiModel(description = "反馈处理")
public class MessFeedbackHandleReq {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "处理意见")
    @NotBlank(message = "请填写处理意见")
    @Length(max = 200, message = "审批意见最多200字")
    private String handleOpinion;
}
