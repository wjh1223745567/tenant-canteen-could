package com.iotinall.canteen.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 营养档案 能量和营养元素计算
 *
 * @author loki
 * @date 2020/04/14 17:51
 */
@Service
public interface NutritionCalculate {
    /**
     * 计算每日所需能量
     *
     * @author loki
     * @date 2020/04/14 19:43
     */
    BigDecimal calculateEnergy(NutritionContext context);

    /**
     * 计算每日所需蛋白质
     *
     * @author loki
     * @date 2020/04/15 13:50
     */
    BigDecimal calculateProtein(NutritionContext context);

    /**
     * 计算每日所需脂肪
     *
     * @author loki
     * @date 2020/04/15 13:50
     */
    BigDecimal calculateFat(NutritionContext context);

    /**
     * 计算每日所需碳水化合物
     *
     * @author loki
     * @date 2020/04/15 13:50
     */
    BigDecimal calculateCarbs(NutritionContext context);

    /**
     * 计算每日所膳食纤维
     *
     * @author loki
     * @date 2020/04/15 13:50
     */
    BigDecimal calculateDietaryFiber(NutritionContext context);
}
