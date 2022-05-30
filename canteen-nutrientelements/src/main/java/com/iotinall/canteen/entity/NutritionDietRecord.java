package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@EqualsAndHashCode(callSuper = true, exclude = {"customDish"})
@Data
@Accessors(chain = true)
@Entity
@Table(name = "nutrition_diet_record")
public class NutritionDietRecord extends BaseEntity {

    @Column(name = "employee_id")
    private Long employeeId;

    /**
     * 类型 0-早餐 1-午餐 2-晚餐 3-加餐
     */
    private Integer type;

    /**
     * 0-系统中的菜品 1-用户自定义食物
     */
    private Integer dishType;

    /**
     * 系统食物
     */
    @Column(name = "mess_product_id")
    private Long messProductId;

    /**
     * 自定义食物
     */
    @OneToOne
    @JoinColumn(name = "custom_dish_id", foreignKey = @ForeignKey(name = "null"))
    private NutritionCustomDish customDish;

    /**
     * 食物重量,单位g
     */
    private BigDecimal dishWeight;

    /**
     * 食物所含能量
     */
    private BigDecimal energy;

    /**
     * 饮食日期
     */
    private LocalDate recordDate;

    /**
     * 排序
     */
    private Integer sort;
}
