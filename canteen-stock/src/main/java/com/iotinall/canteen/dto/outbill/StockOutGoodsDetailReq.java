package com.iotinall.canteen.dto.outbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 出库明细详情
 *
 * @author loki
 * @date 2020/05/04 16:45
 */
@Data
@ApiModel(description = "出库明细")
public class StockOutGoodsDetailReq implements Serializable {

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "出库数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "出库当时库存数量")
    private BigDecimal stockAmount;

    @ApiModelProperty(value = "对应入库单据商品入库明细ID")
    private Long inBillDetailId;

    @ApiModelProperty(value = "用户出库商品明细")
    private List<StockOutUserDetailReq> userDetail;
}
