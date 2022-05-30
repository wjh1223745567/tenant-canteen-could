package com.iotinall.canteen.dto.messfeedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
* @author xin-bing
* @date 2019-10-26 17:23:08
*/
@Data
@ApiModel(description = "查询mess_feedback条件")
public class MessFeedbackQueryCriteria {
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "开始日器")
    private LocalDateTime beginDate;
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endDate;
}