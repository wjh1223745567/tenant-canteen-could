package com.iotinall.canteen.utils;

import java.math.BigDecimal;

/**
 * 分转元或者元转分
 *
 * @author loki
 * @date 2021/6/23 16:40
 **/
public class MoneyUtil {
    /**
     * 元转分，确保price保留两位有效数字
     */
    public static long yuan2Fen(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    /**
     * 分转元，转换为bigDecimal在toString
     */
    public static BigDecimal fen2Yuan(Long price) {
        return BigDecimal.valueOf(price)
                .divide(new BigDecimal(100), BigDecimal.ROUND_UNNECESSARY);
    }
}
