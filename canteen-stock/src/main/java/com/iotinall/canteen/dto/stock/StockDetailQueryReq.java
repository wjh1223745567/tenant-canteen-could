package com.iotinall.canteen.dto.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 库存明细查询
 *
 * @author loki
 * @date 2020/08/31 20:16
 */
@Data
@ApiModel(value = "查询库存明细")
public class StockDetailQueryReq implements Serializable {
    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "商品类型")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品编号或者名称")
    private String keyword;
}
