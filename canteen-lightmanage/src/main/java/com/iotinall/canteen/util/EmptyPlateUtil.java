package com.iotinall.canteen.util;

/**
 * 光盘行动工具类
 *
 * @author loki
 * @date 2021/05/25 19:24
 */
public class EmptyPlateUtil {
    /**
     * 获取坐标点
     *
     * @author loki
     * @date 2021/04/17 14:48
     */
    public static int getPoint(Double x, Integer v) {
        long y = Math.round(x * v);
        return (int) y;
    }
}
