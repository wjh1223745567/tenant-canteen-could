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
import java.time.LocalDateTime;

/**
 * 饮食报告
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "nutrition_diet_report")
public class NutritionDietReport extends BaseEntity {

    @Column(name = "employee_id")
    private Long employeeId;

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


    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 获取当日能量摄入提示语
     *
     * @author loki
     * @date 2020/04/16 21:30
     */
    public String getEnergyTips() {
        Integer score = Integer.valueOf(this.energyScore);
        if (0 <= score && score < 50) {
            return "omg，三餐这样吃下去你会成仙的^^";
        } else if (50 <= score && score < 60) {
            return "再努力以下，离及格不远了";
        } else if (60 <= score && score < 70) {
            return "及格不易，且吃且珍惜";
        } else if (70 <= score && score < 80) {
            return "恭喜你，成功击败了隔壁的王阿姨";
        } else if (80 <= score && score < 90) {
            return "恭喜你成为一名合格的吃货";
        } else if (90 <= score && score < 100) {
            return "你莫非就是传中的隔壁家的孩子";
        } else if (100 <= score) {
            return "oh no,你再这样吃下去，你会变成一个胖纸";
        }
        return "";
    }

    /**
     * 三餐的提示语
     *
     * @author loki
     * @date 2020/04/16 21:31
     */
    public String getMealTips(BigDecimal plan, BigDecimal actual) {
        if (actual.compareTo(plan) == -1) {

            return "您还能摄入" + plan.subtract(actual) + "千卡能量，加油！";
        } else if (actual.compareTo(plan) == 1) {
            //实际摄入大于等于推荐摄入
            return "您摄入的能量已经超出推荐摄入量，建议控制主食、甜食、脂肪等高能量食物摄入，否则你要变胖啦！";
        } else {
            return "您摄入的能量已达标,请继续保持";
        }
    }
}
