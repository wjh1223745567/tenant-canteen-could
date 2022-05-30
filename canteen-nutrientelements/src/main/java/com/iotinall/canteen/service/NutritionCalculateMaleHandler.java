package com.iotinall.canteen.service;


import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.dto.person.PersonRecordDTO;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.utils.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * 男性每日计划所需能量计算
 *
 * @author loki
 * @date 2020/04/14 19:41
 */
public class NutritionCalculateMaleHandler extends NutritionCalculateWrapper {
    private static final BigDecimal CONSTANT = new BigDecimal(67);
    private static final BigDecimal WEIGHT_WEIGHT = new BigDecimal("13.73");
    private static final BigDecimal HEIGHT_WEIGHT = new BigDecimal(5);
    private static final BigDecimal AGE_WEIGHT = new BigDecimal("6.9");

    /**
     * 维持当前身高体重一天需要摄入的总能量计算
     *
     * @author loki
     * @date 2020/04/14 21:11
     */
    @Override
    public BigDecimal calculateEnergy(NutritionContext context) {
        this.getPersonSportLevel(context);

        this.calculateX(context);

        this.calculateY(context);

        return this.calculateIntakeEnergy(context);
    }

    /**
     * 根据活动水平计算一天需要摄入的总能量
     *
     * @author loki
     * @date 2020/04/14 20:51
     */
    private void calculateX(NutritionContext context) {
        BigDecimal metabolicRate = this.calculateMetabolicRate(new BigDecimal(context.getPerson().getPersonWeight()), new BigDecimal(context.getPerson().getPersonHeight()), context.getPerson().getAge(context.getQueryDate()));
        context.setX(BigDecimalUtil.convert(metabolicRate).multiply(context.getSportLevel()));
    }

    /**
     * 根据活动水平计算一天需要摄入的总能量
     *
     * @author loki
     * @date 2020/04/14 20:51
     */
    private void calculateY(NutritionContext context) {
        NutritionReferenceEnergyRepository energyRepository = SpringContextUtil.getBean(NutritionReferenceEnergyRepository.class);

        PersonRecordDTO person = context.getPerson();
        Integer type = null == person.getPhysiologicalState() ? 0 : person.getPhysiologicalState();
        SysReferenceEnergy referenceEnergy = null;
        if (type == Constants.DEFAULT) {
            referenceEnergy = energyRepository.queryEnergy(type, person.getAge(context.getQueryDate()), person.getGender(), person.getStrengthLevel());
        }

        context.setY(null == referenceEnergy ? BigDecimal.ZERO : new BigDecimal(referenceEnergy.getEer()));
    }

    /**
     * 代谢率
     *
     * @author loki
     * @date 2020/04/14 20:08
     */
    private BigDecimal calculateMetabolicRate(BigDecimal weight, BigDecimal height, Integer age) {
        BigDecimal _weight = WEIGHT_WEIGHT.multiply(weight);
        BigDecimal _height = HEIGHT_WEIGHT.multiply(height);

        BigDecimal _age = AGE_WEIGHT.multiply(new BigDecimal(age));

        return CONSTANT.add(_weight).add(_height).subtract(_age);
    }

    /**
     * 获取员工活动水平 年龄 + 性别 + 活动等级
     *
     * @author loki
     * @date 2020/04/15 9:18
     */
    private void getPersonSportLevel(NutritionContext context) {
        NutritionReferenceStrengthRepository strengthRepository = SpringContextUtil.getBean(NutritionReferenceStrengthRepository.class);

        PersonRecordDTO person = context.getPerson();
        Integer type = null == person.getPhysiologicalState() ? 0 : person.getPhysiologicalState();
        SysReferenceStrength referenceStrength = null;
        if (type == Constants.DEFAULT) {
            referenceStrength = strengthRepository.queryStrength(type, person.getAge(context.getQueryDate()), person.getGender(), person.getStrengthLevel());
        }

        context.setSportLevel(null == referenceStrength ? BigDecimal.ZERO : referenceStrength.getRatio());
    }

    /**
     * 计算每日所需蛋白质量
     *
     * @author loki
     * @date 2020/04/15 14:15
     */
    @Override
    public BigDecimal calculateProtein(NutritionContext context) {
        return super.calculateProtein(context, this.getRecommendProtein(context));
    }

    /**
     * 获取推荐的蛋白质
     *
     * @author loki
     * @date 2020/04/15 14:30
     */
    private BigDecimal getRecommendProtein(NutritionContext context) {
        NutritionReferenceProteinRepository proteinRepository = SpringContextUtil.getBean(NutritionReferenceProteinRepository.class);
        PersonRecordDTO person = context.getPerson();
        Integer type = null == person.getPhysiologicalState() ? 0 : person.getPhysiologicalState();

        SysReferenceProtein referenceProtein = null;
        if (type == Constants.DEFAULT) {
            referenceProtein = proteinRepository.queryProtein(type, person.getAge(context.getQueryDate()), person.getGender());
        }
        return null == referenceProtein ? BigDecimal.ZERO : new BigDecimal(referenceProtein.getProteinRni());
    }

    @Override
    public BigDecimal calculateFat(NutritionContext context) {
        return super.calculateFat(context, this.getRecommendFat(context));
    }

    /**
     * 获取推荐的蛋白质
     *
     * @author loki
     * @date 2020/04/15 14:30
     */
    private SysReferenceFat getRecommendFat(NutritionContext context) {
        NutritionReferenceFatRepository fatRepository = SpringContextUtil.getBean(NutritionReferenceFatRepository.class);
        PersonRecordDTO person = context.getPerson();
        Integer type = null == person.getPhysiologicalState() ? 0 : person.getPhysiologicalState();
        if (type == Constants.DEFAULT) {
            return fatRepository.queryFat(type, person.getAge(context.getQueryDate()), person.getGender());
        }
        return null;
    }

    @Override
    public BigDecimal calculateCarbs(NutritionContext context) {
        return super.calculateCarbs(context, this.getRecommendCarbs(context));
    }

    /**
     * 获取推荐的蛋白质
     *
     * @author loki
     * @date 2020/04/15 14:30
     */
    private SysReferenceCholesterol getRecommendCarbs(NutritionContext context) {
        NutritionReferenceCarbsRepository carbsRepository = SpringContextUtil.getBean(NutritionReferenceCarbsRepository.class);
        PersonRecordDTO person = context.getPerson();
        Integer type = null == person.getPhysiologicalState() ? 0 : person.getPhysiologicalState();
        if (type == Constants.DEFAULT) {
            return carbsRepository.queryCarbs(type, person.getAge(context.getQueryDate()), person.getGender());
        }
        return null;
    }

    @Override
    public BigDecimal calculateDietaryFiber(NutritionContext context) {
        return super.calculateDietaryFiber(context);
    }
}
