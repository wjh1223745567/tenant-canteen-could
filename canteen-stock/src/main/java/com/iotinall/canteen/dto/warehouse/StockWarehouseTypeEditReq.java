package com.iotinall.canteen.dto.warehouse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 编辑仓库类型请求参数
 **/
@Data
@ApiModel(description = "编辑仓库类型请求参数")
public class StockWarehouseTypeEditReq implements Serializable {
    @ApiModelProperty(value = "主键ID")
    @NotNull(message = "主键ID不能为空")
    private Long id;

    @ApiModelProperty(value = "仓库类型名称", dataType = "string")
    @NotBlank(message = "仓库类型名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述", dataType = "string")
    private String remark;
}
