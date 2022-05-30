package com.iotinall.canteen.dto.suppliertype;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加供应商类型请求参数
 */
@ApiModel(value = "添加供应商类型请求参数")
@Data
public class StockSupplierV2TypeAddReq {
    @ApiModelProperty(value = "供应商类型名称")
    @NotBlank(message = "类型名称不能为空")
    private String name;

    @ApiModelProperty(value = "供应商类型描述")
    private String remark;
}
