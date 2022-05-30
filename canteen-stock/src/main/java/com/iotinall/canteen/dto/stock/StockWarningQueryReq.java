package com.iotinall.canteen.dto.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预警查询参数
 *
 * @author loki
 * @date 2020/09/10 14:53
 */
@ApiModel(value = "库存预警查询参数")
@Data
public class StockWarningQueryReq implements Serializable {
    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "商品类型ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "预警类型 0-库存预警 1-保质期预警")
    private Integer warningType;

    @ApiModelProperty(value = "商品名称")
    private String keyword;
}
