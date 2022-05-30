package com.iotinall.canteen.dto.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "供应商小程序请求参数")
@Data
public class StockSupplierV2Req {
    @ApiModelProperty(value = "供应商类型Id")
    private Long typeId;

    @ApiModelProperty(value = "信誉等级")
    private Integer credit;

    @ApiModelProperty(value = "供应商名称")
    private String name;
}
