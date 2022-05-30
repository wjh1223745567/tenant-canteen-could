package com.iotinall.canteen.utils;

import java.math.BigDecimal;

/**
 * BigDecimal 工具类
 *
 * @author loki
 * @date 2020/04/15 21:08
 */
public class BigDecimalUtil {
    /**
     * 保留一位小数,四舍五入
     *
     * @author loki
     * @date 2020/04/15 21:08
     */
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2) {
        if (value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return divide(value1, value2, 0);
    }

    /**
     * 保留一位小数,四舍五入
     *
     * @author loki
     * @date 2020/04/15 21:08
     */
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2, Integer scale) {
        if (value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, scale, BigDecimal.ROUND_HALF_UP);
    }
}
