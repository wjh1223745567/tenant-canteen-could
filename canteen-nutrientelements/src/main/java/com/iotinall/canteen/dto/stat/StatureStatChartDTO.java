package com.iotinall.canteen.dto.stat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 身材记录统计
 *
 * @author loki
 * @date 2020/04/10 15:36
 */
@Data
@Accessors(chain = true)
public class StatureStatChartDTO implements Serializable {
    private String label;

    private String date;

    private BigDecimal value;

    private String unit;
}
