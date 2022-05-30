package com.iotinall.canteen.dto.messfeedback;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 添加意见反馈
 * @author WJH
 * @date 2019/11/611:52
 */
@Setter
@Getter
public class MessFeedbackReq {

    @ApiModelProperty(value = "反馈类型", name = "feedType")
    @Length(max = 255, message = "反馈类型字符长度不能超过255个字符")
    @NotBlank(message = "反馈类型不能为空")
    private String feedType;

    @ApiModelProperty(value = "意见内容", name = "content", required = true)
    @Length(max = 255 , message = "意见内容长度不能超过255个字符")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "是否匿名", name = "anonymous")
    private Boolean anonymous;

}
