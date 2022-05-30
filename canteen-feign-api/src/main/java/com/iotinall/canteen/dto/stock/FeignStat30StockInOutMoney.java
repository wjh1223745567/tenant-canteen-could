package com.iotinall.canteen.dto.stock;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 出入库金额
 *
 * @author loki
 * @date 2021/02/24 16:25
 */
@Data
@NoArgsConstructor
public class FeignStat30StockInOutMoney {
    private BigDecimal inAmount = BigDecimal.ZERO;
    private BigDecimal outAmount = BigDecimal.ZERO;
    private String date;

    public FeignStat30StockInOutMoney(BigDecimal in, BigDecimal out) {
        this.inAmount = in;
        this.outAmount = out;
    }
}
