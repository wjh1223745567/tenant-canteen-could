package com.iotinall.canteen.dto.suppliertype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改供应商类型请求参数
 */
@Data
@ApiModel(value = "修改供应商类型请求参数")
public class StockSupplierV2TypeEditReq {
    @ApiModelProperty(value = "供应商类型编号", required = true)
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "供应商类型名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述不能为空", required = true)
    private String remark;

}
