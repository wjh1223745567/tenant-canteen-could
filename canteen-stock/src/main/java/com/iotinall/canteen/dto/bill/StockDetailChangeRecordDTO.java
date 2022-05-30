package com.iotinall.canteen.dto.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存明细变动
 *
 * @author loki
 * @date 2020/09/04 11:11
 */
@ApiModel(description = "库存明细变动记录")
@Data
public class StockDetailChangeRecordDTO implements Serializable {
    @ApiModelProperty(value = "入库单据号")
    private String inBillNo;

    @ApiModelProperty(value = "数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "单据日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;
}
