package com.iotinall.canteen.service;

import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.entity.SysReferenceCholesterol;
import com.iotinall.canteen.entity.SysReferenceFat;
import com.iotinall.canteen.utils.BigDecimalUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 营养档案 每日能量计算
 *
 * @author loki
 * @date 2020/04/15 13:46
 */
@Slf4j
public class NutritionCalculateWrapper implements NutritionCalculate {
    protected BigDecimal HUNDRED = new BigDecimal(100);
    @Override
    public BigDecimal calculateEnergy(NutritionContext context) {
        return null;
    }

    /**
     * 计算能量
     *
     * @author loki
     * @date 2020/04/15 18:10
     */
    public BigDecimal calculateIntakeEnergy(NutritionContext context) {
        Integer target = context.getPerson().getBodyTarget();
        BigDecimal intake = BigDecimal.ZERO;
        if (target == Constants.FAT_REDUCTION) {
            intake = context.getX().min(context.getY());
        } else if (target == Constants.INCREASING_MUSCLE) {
            intake = context.getX().max(context.getY());
        } else if (target == Constants.KEEP) {
            //intake = context.getX().add(context.getSports());
            intake = context.getX(); //这里不加上sports，因为sports是动态改变的，每次查询计划摄入energy时加上
        }
        log.info("计划摄入能量计算结果:");
        log.info("x:{}", context.getX());
        log.info("y:{}", context.getY());
        log.info("sports:{}", context.getSports());
        log.info("energy:{}", intake.setScale(2, BigDecimal.ROUND_HALF_UP));
        // 将添加运动消耗的部分迁移到这里
        BigDecimal intakeEnergy = SpringContextUtil.getBean(NutritionCalculateBiz.class).getIntakeEnergy(context.getEmployee(), intake, context.getQueryDate());
        return intakeEnergy;
    }

    @Override
    public BigDecimal calculateProtein(NutritionContext context) {
        return null;
    }

    /**
     * 计算蛋白质
     *
     * @author loki
     * @date 2020/04/15 15:57
     */
    public BigDecimal calculateProtein(NutritionContext context, BigDecimal proteinRni) {
        BigDecimal PROTEIN_WEIGHT = new BigDecimal("0.15");
        Integer target = context.getPerson().getBodyTarget();
        BigDecimal intake = BigDecimal.ZERO;
        if (target == Constants.FAT_REDUCTION || target == Constants.INCREASING_MUSCLE) {
            BigDecimal x = this.calculateEnergy(context);
            intake = BigDecimalUtil.divide(proteinRni.max(x.multiply(PROTEIN_WEIGHT)), new BigDecimal(4));
        } else if (target == Constants.KEEP) {
            intake = proteinRni;
        }

        return BigDecimalUtil.convert(intake);
    }

    @Override
    public BigDecimal calculateFat(NutritionContext context) {
        return null;
    }

    /**
     * 计算脂肪
     *
     * @author loki
     * @date 2020/04/15 15:53
     */
    public BigDecimal calculateFat(NutritionContext context, SysReferenceFat referenceFat) {
        BigDecimal fatMin = BigDecimal.ZERO;
        BigDecimal fatMax = BigDecimal.ZERO;
        BigDecimal FAT_WEIGHT = new BigDecimal(9);

        if (null != referenceFat) {
            fatMin = new BigDecimal(referenceFat.getFatMin()).divide(HUNDRED);
            fatMax = new BigDecimal(referenceFat.getFatMax()).divide(HUNDRED);
        }

        Integer target = context.getPerson().getBodyTarget();
        BigDecimal x = this.calculateEnergy(context);
        BigDecimal intakeFat = BigDecimalUtil.divide(x.multiply(fatMin), FAT_WEIGHT);
        BigDecimal intake = BigDecimal.ZERO;
        if (target == Constants.FAT_REDUCTION || target == Constants.INCREASING_MUSCLE) {
            intake = intakeFat;
        } else if (target == Constants.KEEP) {
            intake = BigDecimalUtil.divide((intakeFat.add(BigDecimalUtil.divide(x.multiply(fatMax), FAT_WEIGHT)))
                    , new BigDecimal(2));
        }
        return BigDecimalUtil.convert(intake);
    }

    @Override
    public BigDecimal calculateCarbs(NutritionContext context) {
        return null;
    }

    /**
     * 计算碳水化合物
     *
     * @author loki
     * @date 2020/04/15 16:00
     */
    public BigDecimal calculateCarbs(NutritionContext context, SysReferenceCholesterol referenceCarbs) {
        BigDecimal carbsMin = BigDecimal.ZERO;
        BigDecimal carbsMax = BigDecimal.ZERO;
        BigDecimal CARBS_WEIGHT = new BigDecimal(4);

        if (null != referenceCarbs) {
            carbsMin = new BigDecimal(referenceCarbs.getChoMin()).divide(HUNDRED);
            carbsMax = new BigDecimal(referenceCarbs.getChoMax()).divide(HUNDRED);
        }

        BigDecimal x = this.calculateEnergy(context);
        BigDecimal min = BigDecimalUtil.divide(x.multiply(carbsMin), CARBS_WEIGHT);
        BigDecimal max = BigDecimalUtil.divide(x.multiply(carbsMax), CARBS_WEIGHT);
        Integer target = context.getPerson().getBodyTarget();
        BigDecimal intake = BigDecimal.ZERO;
        if (target == Constants.FAT_REDUCTION) {
            intake = min;
        } else if (target == Constants.KEEP) {
            intake = BigDecimalUtil.divide((min.add(max)), new BigDecimal(2));
        } else if (target == Constants.INCREASING_MUSCLE) {
            intake = max;
        }
        return BigDecimalUtil.convert(intake);
    }

    @Override
    public BigDecimal calculateDietaryFiber(NutritionContext context) {
        return new BigDecimal(25);
    }
}
