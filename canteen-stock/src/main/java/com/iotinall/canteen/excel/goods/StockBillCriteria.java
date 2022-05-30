package com.iotinall.canteen.excel.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "入库单据excel查询参数")
public class StockBillCriteria {

    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "供应商")
    private Long supplierId;

}
