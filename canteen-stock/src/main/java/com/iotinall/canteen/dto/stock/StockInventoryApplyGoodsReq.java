package com.iotinall.canteen.dto.stock;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "盘点申请请求参数明细")
public class StockInventoryApplyGoodsReq implements Serializable {

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "盘点数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "库存数量")
    private BigDecimal stockAmount;
}
