package com.iotinall.canteen.dto.stock;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 食堂大屏-仓库实况
 *
 * @author loki
 * @date 2021/7/13 10:05
 **/
@Data
@Accessors(chain = true)
public class FeignTodayStockMoneyDTO implements Serializable {
    private BigDecimal inAmountToday;
    private BigDecimal outAmountToday;
}
