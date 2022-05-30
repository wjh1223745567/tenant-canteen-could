package com.iotinall.canteen.dto.dish;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统原材料查询条件
 */
@ApiModel(value = "原料管理请求参数")
@Data
public class SysMaterialQueryReq {
    @ApiModelProperty(value = "名称，别名")
    private String keyword;

    @ApiModelProperty
    private String materialTypeId;
}
