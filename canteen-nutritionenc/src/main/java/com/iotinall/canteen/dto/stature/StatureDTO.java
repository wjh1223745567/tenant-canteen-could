package com.iotinall.canteen.dto.stature;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 身材记录返回对象
 *
 * @author loki
 * @date 2020/04/10 17:14
 */
@Data
@Accessors(chain = true)
public class StatureDTO {
    private String code;
    private String name;
    private BigDecimal value;
    private String unit;
}
