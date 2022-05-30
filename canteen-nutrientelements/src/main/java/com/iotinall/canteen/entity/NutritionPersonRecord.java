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
 * 基础信息
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "nutrition_person_record")
public class NutritionPersonRecord extends BaseEntity {

    @Column(name = "employee_id")
    private Long employeeId;

    /**
     * 性别 0-女 1-男 可以和员工表性别不一样,营养档案以这个为准
     */
    @Column(nullable = false)
    private Integer gender;

    /**
     * 出生日期
     */
    @Column(nullable = false)
    private LocalDate birthDate;

    /**
     * 怀孕日期
     */
    private LocalDate pregnancyDate;

    /**
     * 身高 单位cm
     */
    private Integer personHeight;

    /**
     * 体重 单位kg
     */
    private Integer personWeight;

    /**
     * 身材目标 0-减脂 1-增肌 2-保持体形
     */
    private Integer bodyTarget;

    /**
     * 活动水平类型 0-低强度 1-中强度 2-高强度
     */
    private Integer strengthLevel;

    /**
     * 生理状态 1-怀孕 2-乳母 默认 0
     */
    private Integer physiologicalState;

    /**
     * 疾病
     */
    @Column(columnDefinition = "text")
    private String disease;

    /**
     * 计划摄入量
     */
    private BigDecimal intake;

}
