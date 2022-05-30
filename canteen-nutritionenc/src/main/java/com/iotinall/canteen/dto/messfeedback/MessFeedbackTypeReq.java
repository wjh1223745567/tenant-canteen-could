package com.iotinall.canteen.dto.messfeedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xin-bing
 * @date 10/27/2019 20:16
 */
public class MessFeedbackTypeReq {
    @Data
    @ApiModel(description = "添加反馈类型请求")
    public static class AddReq {
        @ApiModelProperty(value = "反馈类型")
        private String label;

        @ApiModelProperty(value = "描述")
        private String remark;
    }
    @Data
    @ApiModel(description = "修改反馈类型请求")
    public static class EditReq {
        @ApiModelProperty(value = "主键")
        private Long id;
        @ApiModelProperty(value = "反馈类型")
        private String label;
        @ApiModelProperty(value = "描述")
        private String remark;
    }
}
