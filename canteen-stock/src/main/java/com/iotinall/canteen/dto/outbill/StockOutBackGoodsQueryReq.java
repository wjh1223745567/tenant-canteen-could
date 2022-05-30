package com.iotinall.canteen.dto.outbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 领用退库申请请求参数
 *
 * @author loki
 * @date 2020/05/03 11:21
 */
@Data
@ApiModel(description = "领用退库查询商品")
public class StockOutBackGoodsQueryReq implements Serializable {
    @ApiModelProperty(value = "出库单据")
    private String outBillNo;

    @ApiModelProperty(value = "货品类型")
    private Long goodsTypeId;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "商品编号或者名称")
    private String keyword;
}
