package com.iotinall.canteen.dto.outbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 仓管一键出库
 * 一件商品对应对个领用人
 *
 * @author loki
 * @date 2020/05/04 16:45
 */
@Data
@ApiModel(description = "用户出库商品明细")
public class StockOutUserDetailReq implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "出库数量")
    private BigDecimal amount;
}
