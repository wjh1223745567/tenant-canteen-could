package com.iotinall.canteen.dto.goods;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品列表查询条件
 *
 * @author loki
 * @date 2020/09/12 10:38
 */
@Data
@ApiModel(description = "商品列表查询条件")
public class StockGoodsQueryReq implements Serializable {
    @ApiModelProperty(value = "商品类型ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "输入商品编号或名称查询")
    private String keyword;
}
