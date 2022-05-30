package com.iotinall.canteen.dto.warehouse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加仓库类型请求参数
 **/
@Data
@ApiModel(description = "添加仓库类型请求参数")
public class StockWarehouseTypeAddReq implements Serializable {

    @ApiModelProperty(value = "仓库类型名称", dataType = "string")
    @NotBlank(message = "仓库类型名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述", dataType = "string")
    private String remark;
}
