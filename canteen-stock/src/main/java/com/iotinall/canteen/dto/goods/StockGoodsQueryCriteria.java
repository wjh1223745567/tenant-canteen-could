package com.iotinall.canteen.dto.goods;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("description = 查询类型商品条件")
public class StockGoodsQueryCriteria {
    @ApiModelProperty(value = "货品类型Id")
    private Long goodsTypeId;

    @ApiModelProperty(value = "仓库Id")
    private Long warehouseId;

    @ApiModelProperty(value = "货品名称")
    private String name;
}
