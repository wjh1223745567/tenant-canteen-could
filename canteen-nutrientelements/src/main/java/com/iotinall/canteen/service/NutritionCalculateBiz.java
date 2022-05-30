package com.iotinall.canteen.service;

import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.dto.diet.NutritionDiffDTO;
import com.iotinall.canteen.dto.person.PersonRecordDTO;
import com.iotinall.canteen.entity.NutritionDietRecord;
import com.iotinall.canteen.entity.NutritionPersonRecord;
import com.iotinall.canteen.repository.NutritionDietRecordRepository;
import com.iotinall.canteen.repository.NutritionPersonRecordRepository;
import com.iotinall.canteen.utils.BigDecimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 推荐摄入营养元素计算处理类
 * 实际摄入营养元素计算处理类
 *
 * @author loki
 * @date 2020/04/15 9:03
 */
@Slf4j
@Service
public class NutritionCalculateBiz {
    @Resource
    private FeignMessProductService feignMessProductService;
    @Resource
    NutritionPersonRecordRepository personRecordRepository;
    @Resource
    NutritionDietRecordRepository dietRecordRepository;
    @Resource
    NutritionSportRecordService sportRecordService;

    private static final int PERSON_GENDER_MALE = 0;
    private static final int PERSON_GENDER_FEMALE = 1;

    /**
     * 推荐摄入能量，蛋白质，脂肪，碳水化合物，膳食纤维推荐摄入量计算,能量不包括运动消耗
     *
     * @author loki
     * @date 2020/04/15 9:04
     */
    public BigDecimal calculate(Long employeeId, Integer queryType) {
        return calculate(employeeId, LocalDate.now(), queryType);
    }

    /**
     * 推荐摄入能量，蛋白质，脂肪，碳水化合物，膳食纤维推荐摄入量计算,能量不包括运动消耗
     *
     * @author loki
     * @date 2020/04/15 9:04
     */
    public BigDecimal calculate(Long employeeId, LocalDate date, Integer queryType) {
        return new NutritionContext()
                .setQueryDate(date)
                .setQueryType(queryType)
                .setEmployee(employeeId)
                .setPerson(this.findNutritionArchiveRecord(employeeId))
                .calculate();
    }

    /**
     * 能量，蛋白质，脂肪，碳水化合物，膳食纤维推荐摄入量计算
     *
     * @author loki
     * @date 2020/04/15 9:04
     */
    public NutritionDiffDTO calculateAll(Long employeeId, LocalDate queryDate) {
        NutritionContext context = new NutritionContext();
        context.setQueryDate(queryDate);
        context.setEmployee(employeeId);
        context.setPerson(this.findNutritionArchiveRecord(employeeId));
        return this.calculateAll(context);
    }

    /**
     * 能量，蛋白质，脂肪，碳水化合物，膳食纤维推荐摄入量计算
     *
     * @author loki
     * @date 2020/04/15 9:04
     */
    private NutritionDiffDTO calculateAll(NutritionContext context) {
        NutritionDiffDTO nutrition = new NutritionDiffDTO();
        context.setQueryType(Constants.CALCULATE_TYPE_ENERGY);
        nutrition.setPlanEnergy(context.getPerson() != null && context.getPerson().getIntake() != null ? context.getPerson().getIntake() : context.calculate());

        context.setQueryType(Constants.CALCULATE_TYPE_PROTEIN);
        nutrition.setPlanProtein(context.calculate());

        context.setQueryType(Constants.CALCULATE_TYPE_FAT);
        nutrition.setPlanFat(context.calculate());

        context.setQueryType(Constants.CALCULATE_TYPE_CARBS);
        nutrition.setPlanCarbs(context.calculate());

        context.setQueryType(Constants.CALCULATE_TYPE_DIETARY_FIBER);
        nutrition.setPlanDietaryFiber(context.calculate());

        return nutrition;
    }

    /**
     * 计算推荐摄入能量
     * 保持型身材需要加上运动消耗的能量
     *
     * @author loki
     * @date 2020/04/17 16:02
     */
    public BigDecimal getIntakeEnergy(Long employeeId, BigDecimal plan) {
        return getIntakeEnergy(employeeId, plan, LocalDate.now());
    }

    /**
     * 计算推荐摄入能量
     * 保持型身材需要加上运动消耗的能量
     *
     * @author loki
     * @date 2020/04/17 16:02
     */
    public BigDecimal getIntakeEnergy(Long employeeId, BigDecimal plan, LocalDate date) {
        if (this.isBodyTargetKeep(employeeId)) {
            BigDecimal sportBurnCalories = sportRecordService.calculateBurnCalories(employeeId, date);
            plan = plan.add(sportBurnCalories);
        }
        return BigDecimalUtil.convert(plan);
    }

    /**
     * 是否为保持型身材
     *
     * @author loki
     * @date 2020/04/15 21:51
     */
    private boolean isBodyTargetKeep(Long employeeId) {
        PersonRecordDTO record = this.findNutritionArchiveRecord(employeeId);
        return null != record && (record.getBodyTarget() == Constants.KEEP || record.getBodyTarget() == Constants.INCREASING_MUSCLE);
    }


    /**
     * 个人营养档案
     *
     * @author loki
     * @date 2020/04/15 9:08
     */
    private PersonRecordDTO findNutritionArchiveRecord(Long employeeId) {
        NutritionPersonRecord record = personRecordRepository.queryByEmployeeId(employeeId);
        PersonRecordDTO person = null;
        if (null != record) {
            person = new PersonRecordDTO();
            BeanUtils.copyProperties(record, person);

            /**
             * 处理性别,系统营养档案基础表中的性别和智慧餐厅中的性别相反
             * 智慧餐厅 0-女 1-男
             * 营养档案基础表 0-男 1-女
             */
            person.setGender(record.getGender() == Constants.MALE ? PERSON_GENDER_FEMALE : PERSON_GENDER_MALE);
        }
        return person;
    }

    /**
     * 计算已经摄入的能量
     *
     * @author loki
     * @date 2020/04/16 9:40
     */
    public NutritionDiffDTO calculateNutritionToken(LocalDate recordDate) {
        Long empId = SecurityUtils.getUserId();
        //获取该用户的饮食记录
        List<NutritionDietRecord> dietRecords = dietRecordRepository.queryByEmployeeIdAndRecordDate(empId, recordDate);
        return this.calculateNutritionToken(dietRecords);
    }

    /**
     * 计算已经摄入的能量
     *
     * @author loki
     * @date 2020/04/16 9:40
     */
    public NutritionDiffDTO calculateNutritionToken(Long employeeId, LocalDate recordDate) {
        //获取该用户的饮食记录
        List<NutritionDietRecord> dietRecords = dietRecordRepository.queryByEmployeeIdAndRecordDate(employeeId, recordDate);
        return this.calculateNutritionToken(dietRecords);
    }

    /**
     * 计算已经摄入的能量
     *
     * @param diets 饮食记录
     * @author loki
     * @date 2020/04/16 9:40
     */
    public NutritionDiffDTO calculateNutritionToken(List<NutritionDietRecord> diets) {
        BigDecimal totalEnergy = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;
        BigDecimal totalCarbs = BigDecimal.ZERO;
        BigDecimal totalFiber = BigDecimal.ZERO;

        for (NutritionDietRecord diet : diets) {
            if (diet.getDishType() == Constants.SYS_DISH) {
                NutritionNone nutritionNone = feignMessProductService.findById(diet.getMessProductId());
                if (nutritionNone != null) {
                    totalEnergy = totalEnergy.add(BigDecimalUtil.calculate(diet.getDishWeight(), nutritionNone.getEnergy()));
                    totalProtein = totalProtein.add(BigDecimalUtil.calculate(diet.getDishWeight(), nutritionNone.getProtein()));
                    totalFat = totalFat.add(BigDecimalUtil.calculate(diet.getDishWeight(), nutritionNone.getFat()));
                    totalCarbs = totalCarbs.add(BigDecimalUtil.calculate(diet.getDishWeight(), nutritionNone.getCarbohydrate()));
                    totalFiber = totalFiber.add(BigDecimalUtil.calculate(diet.getDishWeight(), nutritionNone.getDietaryFiber()));
                }
            } else {
                if (null != diet.getCustomDish() && null != diet.getCustomDish().getNutrition()) {
                    totalEnergy = totalEnergy.add(BigDecimalUtil.calculate(diet.getDishWeight(), diet.getCustomDish().getNutrition().getEnergy()));
                    totalProtein = totalProtein.add(BigDecimalUtil.calculate(diet.getDishWeight(), diet.getCustomDish().getNutrition().getProtein()));
                    totalFat = totalFat.add(BigDecimalUtil.calculate(diet.getDishWeight(), diet.getCustomDish().getNutrition().getFat()));
                    totalCarbs = totalCarbs.add(BigDecimalUtil.calculate(diet.getDishWeight(), diet.getCustomDish().getNutrition().getCarbohydrate()));
                    totalFiber = totalFiber.add(BigDecimalUtil.calculate(diet.getDishWeight(), diet.getCustomDish().getNutrition().getDietaryFiber()));
                }
            }
        }

        return new NutritionDiffDTO().setActualEnergy(totalEnergy).setActualProtein(totalProtein)
                .setActualFat(totalFat).setActualCarbs(totalCarbs).setActualDietaryFiber(totalFiber);
    }
}
