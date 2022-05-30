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
     * @author loki
     * @date 2020/04/15 21:08
     */
    public static String convert2Str(BigDecimal value) {
        return convert(value).toString();
    }

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


    /**
     * @author loki
     * @date 2020/04/15 21:08
     */
    public static BigDecimal convert(BigDecimal value) {
        return convert(0, value);
    }

    /**
     * 保留一位小数,四舍五入
     *
     * @author loki
     * @date 2020/04/15 21:08
     */
    public static BigDecimal convert(Integer scale, BigDecimal value) {
        return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 保留一位小数,四舍五入
     *
     * @author loki
     * @date 2020/04/15 21:08
     */
    public static BigDecimal convert(Double value) {
        return new BigDecimal(value + "").setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    private static final BigDecimal HUNDRED = new BigDecimal(100);

    /**
     * 计算weight质量下所含有的能量/蛋白质/碳水化合物/脂肪/膳食纤维
     *
     * @author loki
     * @date 2020/04/15 21:05
     */
    public static BigDecimal calculate(BigDecimal weight, Long per) {
        BigDecimal result;
        if (null == per) {
            result = BigDecimal.ZERO;
        } else {
            // 除以100不会有除不尽的情况
            result = weight.divide(HUNDRED).multiply(new BigDecimal(per));
        }
        return convert(result);
    }

}
