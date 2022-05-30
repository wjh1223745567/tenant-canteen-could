package com.iotinall.canteen.dto.messfeedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 添加mess_feedback请求
 * @author xin-bing
 * @date 2019-10-27 17:23:08
 */
@Data
@ApiModel(description = "添加mess_feedback请求")
public class MessFeedbackAddReq {

    @ApiModelProperty(value = "content", required = true)
    @NotBlank(message = "请填写content")
    private String content;// content

    @ApiModelProperty(value = "anonymous", required = true)
    @NotNull(message = "请填写anonymous")
    private Boolean anonymous;// anonymous

    @ApiModelProperty(value = "feed_type", required = true)
    @NotBlank(message = "请填写feed_type")
    private String feedType;// feed_type

    @ApiModelProperty(value = "status", required = true)
    @NotNull(message = "请填写status")
    private Integer status;// status

    @ApiModelProperty(value = "handle_time")
    private LocalDateTime handleTime;// handle_time

    @ApiModelProperty(value = "createtime", required = true)
    @NotNull(message = "请填写createtime")
    private LocalDateTime createtime;// createtime

    @ApiModelProperty(value = "emp_id", required = true)
    @NotNull(message = "请填写emp_id")
    private Long empId;// emp_id
}