package com.iotinall.canteen.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营养元素差值返回对象
 *
 * @author loki
 * @date 2020/04/15 20:37
 */
@Data
@Accessors(chain = true)
public class NutritionDiffDTO implements Serializable {
    /**
     * 计划摄入能量
     */
    private BigDecimal planEnergy = BigDecimal.ZERO;

    /**
     * 实际摄入能量
     */
    private BigDecimal actualEnergy = BigDecimal.ZERO;

    /**
     * 计划摄入蛋白质
     */
    private BigDecimal planProtein = BigDecimal.ZERO;

    /**
     * 实际摄入蛋白质
     */
    private BigDecimal actualProtein = BigDecimal.ZERO;

    /**
     * 计划摄入脂肪
     */
    private BigDecimal planFat = BigDecimal.ZERO;

    /**
     * 实际摄入脂肪
     */
    private BigDecimal actualFat = BigDecimal.ZERO;

    /**
     * 计划摄入碳水化合物
     */
    private BigDecimal planCarbs = BigDecimal.ZERO;

    /**
     * 实际摄入碳水化合物
     */
    private BigDecimal actualCarbs = BigDecimal.ZERO;

    /**
     * 计划摄入膳食纤维
     */
    private BigDecimal planDietaryFiber = BigDecimal.ZERO;

    /**
     * 计划摄入膳食纤维
     */
    private BigDecimal actualDietaryFiber = BigDecimal.ZERO;
}
