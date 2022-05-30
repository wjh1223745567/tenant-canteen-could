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
 * 每日计划摄入能量和实际摄入能量统计
 * 注:
 * 每天凌晨生成当天计划摄入的能量,统计上一天已摄入的能量
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "nutrition_intake_stat")
public class NutritionIntakeStat extends BaseEntity {

    @Column(name = "employee_id")
    private Long employeeId;

    /**
     * 计划日期
     */
    @Column(name = "plan_date")
    private LocalDate planDate;

    /**
     * 计划摄入能量
     */
    @Column(name = "plan_energy")
    private BigDecimal planEnergy;

    /**
     * 实际摄入能量
     */
    @Column(name = "actual_energy")
    private BigDecimal actualEnergy;

    /**
     * 计划摄入蛋白质
     */
    @Column(name = "plan_protein")
    private BigDecimal planProtein;

    /**
     * 实际摄入蛋白质
     */
    @Column(name = "actual_protein")
    private BigDecimal actualProtein;

    /**
     * 计划摄入脂肪
     */
    @Column(name = "plan_fat")
    private BigDecimal planFat;

    /**
     * 实际摄入脂肪
     */
    @Column(name = "actual_fat")
    private BigDecimal actualFat;

    /**
     * 计划摄入碳水化合物
     */
    @Column(name = "plan_carbs")
    private BigDecimal planCarbs;

    /**
     * 实际摄入碳水化合物
     */
    @Column(name = "actual_carbs")
    private BigDecimal actualCarbs;

    /**
     * 计划摄入膳食纤维
     */
    @Column(name = "plan_dietary_fiber")
    private BigDecimal planDietaryFiber;

    /**
     * 实际摄入膳食纤维
     */
    @Column(name = "actual_dietary_fiber")
    private BigDecimal actualDietaryFiber;

}
