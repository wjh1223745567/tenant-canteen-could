package com.iotinall.canteen.dto.sport;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 运动记录
 *
 * @author loki
 * @date 2020/04/14 10:12
 */
@Accessors(chain = true)
@Data
public class SportRecordDTO implements Serializable {
    private Long id;

    /**
     * 运动项目名称
     */
    private String sportName;

    /**
     * 运动时间 单位秒
     */
    private Integer sportTime;

    /**
     * 运动消耗卡路里 单位kcal,修改了运动时间重新计算
     */
    private BigDecimal burnCalories;
}
