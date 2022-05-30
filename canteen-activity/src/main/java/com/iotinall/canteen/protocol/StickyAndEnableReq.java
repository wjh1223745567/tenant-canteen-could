package com.iotinall.canteen.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "置顶与状态请求参数")
public class StickyAndEnableReq {
    @ApiModelProperty(value = "活动调查的ID")
    @NotNull(message = "活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否置顶")
    private Boolean sticky;

    @ApiModelProperty(value = "是否启用（状态）")
    private Boolean state;

}
