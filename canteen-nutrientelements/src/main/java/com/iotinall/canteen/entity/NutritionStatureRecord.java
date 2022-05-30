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
 * 身材记录
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "nutrition_stature_record")
public class NutritionStatureRecord extends BaseEntity {

    @Column(name = "employee_id")
    private Long employeeId;

    /**
     * 身材记录时间段
     */
    private LocalDate recordDate;

    private String name;

    /**
     * 打卡项目
     */
    private String code;

    /**
     * 评估结果
     */
    private String standard;

    /**
     * 打卡项目值
     */
    private BigDecimal value;

    private String unit;

    /**
     * 排序
     */
    private Integer sort;

}
