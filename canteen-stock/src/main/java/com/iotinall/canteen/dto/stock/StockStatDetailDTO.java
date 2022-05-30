package com.iotinall.canteen.dto.stock;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存汇总明细
 *
 * @author loki
 * @date 2020/09/12 14:33
 */
@Data
@ApiModel(description = "库存汇总明细")
@NoArgsConstructor
@AllArgsConstructor
public class StockStatDetailDTO implements Serializable {
    @ApiModelProperty(value = "数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal count = BigDecimal.ZERO;

    @ApiModelProperty(value = "金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal amount = BigDecimal.ZERO;

    @ApiModelProperty(value = "单价")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal price = BigDecimal.ZERO;
}
