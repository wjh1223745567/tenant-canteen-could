package com.iotinall.canteen.dto.diet;

import lombok.Data;

import java.time.LocalDate;

/**
 * 饮食报告
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@Data
public class NutritionDietReportDTO {
    /**
     * 生成报告日期
     */
    private LocalDate reportDate;

    /**
     * 早餐摄入能量
     */
    private String breakfastEnergy;

    /**
     * 早餐摄入能量占总能量比,保留一位小数
     */
    private String breakfastEnergyPer;

    /**
     * 早餐提示语
     */
    private String breakfastTips;

    /**
     * 午餐摄入能量
     */
    private String lunchEnergy;

    /**
     * 午餐摄入能量占总能量比,保留一位小数
     */
    private String lunchEnergyPer;

    /**
     * 午餐提示语
     */
    private String lunchTips;

    /**
     * 晚餐摄入能量
     */
    private String dinnerEnergy;

    /**
     * 晚餐摄入能量占总能量比,保留一位小数
     */
    private String dinnerEnergyPer;

    /**
     * 晚餐提示语
     */
    private String dinnerTips;

    /**
     * 加餐摄入能量
     */
    private String snackEnergy;

    /**
     * 加餐摄入能量占总能量比,保留一位小数
     */
    private String snackEnergyPer;

    /**
     * 加餐提示语
     */
    private String snackTips;

    /************************营养元素*****************************/
    /**
     * 计划摄入能量
     */
    private String intakeEnergy;

    /**
     * 实际摄入能量
     */
    private String tokenEnergy;

    /**
     * 推荐摄入量占比
     */
    private String energyPer;

    /**
     * 能量计算得分
     */
    private String energyScore;
    /**
     * 能量计算得分提示语
     */
    private String energyTips;

    /**
     * 计划摄入蛋白质
     */
    private String intakeProtein;

    /**
     * 实际摄入蛋白质
     */
    private String tokenProtein;

    /**
     * 推荐摄入蛋白质量占比
     */
    private String proteinPer;

    /**
     * 计划摄入脂肪
     */
    private String intakeFat;

    /**
     * 实际摄入脂肪
     */
    private String tokenFat;

    /**
     * 推荐摄入脂肪量占比
     */
    private String fatPer;

    /**
     * 计划摄入碳水化合物
     */
    private String intakeCarbs;

    /**
     * 实际摄入碳水化合物
     */
    private String tokenCarbs;

    /**
     * 推荐摄入碳水化合物量占比
     */
    private String carbsPer;

    /**
     * 计划摄入膳食纤维
     */
    private String intakeDietaryFiber;

    /**
     * 实际摄入膳食纤维
     */
    private String tokenDietaryFiber;

    /**
     * 推荐摄入膳食纤维量占比
     */
    private String dietaryFiberPer;
}
