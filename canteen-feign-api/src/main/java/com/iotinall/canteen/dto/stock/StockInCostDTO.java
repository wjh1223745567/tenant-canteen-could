package com.iotinall.canteen.dto.stock;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购支出
 *
 * @author loki
 * @date 2021/02/26 11:42
 */
@Data
public class StockInCostDTO {
    private Long typeId;
    private String typeName;
    private BigDecimal amount;
}
