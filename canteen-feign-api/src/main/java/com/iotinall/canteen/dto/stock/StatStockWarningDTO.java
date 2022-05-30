package com.iotinall.canteen.dto.stock;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品预警
 *
 * @author loki
 * @date 2020/09/27 13:50
 */
@Data
public class StatStockWarningDTO implements Serializable {
    private String goodsName;
    private BigDecimal stockAmount;
    private String unit;
}
