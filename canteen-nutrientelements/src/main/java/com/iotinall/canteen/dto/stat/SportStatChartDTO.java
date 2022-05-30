package com.iotinall.canteen.dto.stat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 运动记录统计
 *
 * @author loki
 * @date 2020/04/10 15:36
 */
@Data
@Accessors(chain = true)
public class SportStatChartDTO implements Serializable {
    private String label;

    private String date;

    private String sportName;

    private BigDecimal burnCalories = BigDecimal.ZERO;
}
