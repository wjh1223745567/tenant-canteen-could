package com.iotinall.canteen.dto.messfeedback;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author WJH
 * @date 2019/11/79:07
 */
@Setter
@Getter
@Accessors(chain = true)
public class AppMessFeedbackDto {

    @ApiModelProperty(value = "ID", name = "id")
    private Long id;

    @ApiModelProperty(value = "反馈类型", name = "feedBackType")
    private String feedBackType;

    @ApiModelProperty(value = "反馈内容", name = "content")
    private String content;

    private String createTime;

}
