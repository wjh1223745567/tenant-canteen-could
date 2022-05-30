package com.iotinall.canteen.dto.stat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 今日我的能量统计
 *
 * @author loki
 * @date 2020/04/10 15:36
 */
@Data
@Accessors(chain = true)
public class EnergyStatChartDTO implements Serializable {
    private String label;

    private String date;
    /**
     * 计划
     */
    private BigDecimal plan = BigDecimal.ZERO;
    /**
     * 实际
     */
    private BigDecimal token = BigDecimal.ZERO;
    /**
     * 相差
     */
    private BigDecimal diff = BigDecimal.ZERO;
}
