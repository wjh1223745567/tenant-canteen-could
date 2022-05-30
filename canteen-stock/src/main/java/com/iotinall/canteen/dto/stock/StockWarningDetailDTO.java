package com.iotinall.canteen.dto.stock;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存预警
 *
 * @author loki
 * @date 2020/05/06 19:40
 */
@Data
public class StockWarningDetailDTO {
    @ApiModelProperty(value = "入库单据号")
    private String billNo;

    @ApiModelProperty(value = "入库日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "库存数")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "保质期")
    private LocalDate shelfLife;
}