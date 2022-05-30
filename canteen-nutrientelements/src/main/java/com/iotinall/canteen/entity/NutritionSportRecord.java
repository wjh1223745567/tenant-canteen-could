package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 运动记录
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "nutrition_sport_record")
public class NutritionSportRecord extends BaseEntity {

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

    /**
     * 运动日期
     */
    private LocalDate sportDate;

    /**
     * 员工
     */
    @Column(name = "employee_id")
    private Long employeeId;

}
