package com.iotinall.canteen.common.protocol;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author bingo
 * @date 11/29/2019 10:57
 */
@Data
public class CanteenMealStatis {
    private Long breakfastCount;
    private BigDecimal breakfastAmount;
    private Long lunchCount;
    private BigDecimal lunchAmount;
    private Long dinnerCount;
    private BigDecimal dinnerAmount;
}
