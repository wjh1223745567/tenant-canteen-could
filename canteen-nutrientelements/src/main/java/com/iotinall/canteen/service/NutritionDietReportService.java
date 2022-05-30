package com.iotinall.canteen.service;


import cn.hutool.core.bean.BeanUtil;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.dto.diet.NutritionDietReportDTO;
import com.iotinall.canteen.dto.diet.NutritionDiffDTO;
import com.iotinall.canteen.entity.NutritionDietRecord;
import com.iotinall.canteen.entity.NutritionDietReport;
import com.iotinall.canteen.entity.NutritionIntakeStat;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.repository.NutritionDietRecordRepository;
import com.iotinall.canteen.repository.NutritionDietReportRepository;
import com.iotinall.canteen.repository.NutritionIntakeStatRepository;
import com.iotinall.canteen.utils.BigDecimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 营养档案,饮食报告
 *
 * @author loki
 * @date 2020/04/10 14:33
 */
@Slf4j
@Service
public class NutritionDietReportService {
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignMessProductService feignMessProductService;

    @Resource
    private NutritionIntakeStatRepository intakeStatRepository;
    @Resource
    private NutritionCalculateBiz calculateBiz;
    @Resource
    private NutritionDietReportRepository dietReportRepository;
    @Resource
    private NutritionDietRecordRepository dietRecordRepository;

    private final BigDecimal HUNDRED = new BigDecimal(100);
    private final BigDecimal PERCENT_30 = new BigDecimal("0.3");
    private final BigDecimal PERCENT_40 = new BigDecimal("0.4");

    public void analysis(LocalDate date) {
        Long empId = SecurityUtils.getUserId();
        NutritionDietReport report = dietReportRepository.queryByEmployeeIdAndReportDate(empId, date);
        if (null != report) {
            this.dietReportRepository.delete(report);
        }
        report = new NutritionDietReport();

        //获取饮食记录
        boolean haveDietRecord = Boolean.TRUE;
        List<NutritionDietRecord> dietRecords = dietRecordRepository.queryByEmployeeIdAndRecordDate(empId, date);
        if (CollectionUtils.isEmpty(dietRecords)) {
            log.info("饮食记录为空");
            haveDietRecord = Boolean.FALSE;
        }

        //能量及营养元素统计
        NutritionIntakeStat stat = intakeStatRepository.queryByEmployeeIdAndPlanDate(empId, date);
        if (null == stat) {
            stat = new NutritionIntakeStat();
            //获取计划摄入的营养元素
            NutritionDiffDTO nutrition = calculateBiz.calculateAll(empId, date);
            BeanUtils.copyProperties(nutrition, stat);
            stat.setPlanDate(date);
            stat.setEmployeeId(empId);
            if (haveDietRecord) {
                //获取摄入的营养元素
                NutritionDiffDTO nutritionToken = calculateBiz.calculateNutritionToken(dietRecords);
                stat.setActualEnergy(nutritionToken.getActualEnergy());
                stat.setActualProtein(nutritionToken.getActualProtein());
                stat.setActualFat(nutritionToken.getActualFat());
                stat.setActualCarbs(nutritionToken.getActualCarbs());
                stat.setActualDietaryFiber(nutritionToken.getActualDietaryFiber());
            }
            this.intakeStatRepository.save(stat);
        }

        //营养元素
        this.generateNutrition(report, stat);

        //饮食记录
        this.generateMeal(report, stat, dietRecords);

        report.setReportDate(date);
        report.setCreateTime(LocalDateTime.now());
        report.setEmployeeId(empId);
        this.dietReportRepository.save(report);
    }

    /**
     * 三餐
     *
     * @author loki
     * @date 2020/04/17 9:25
     */
    private void generateMeal(NutritionDietReport report, NutritionIntakeStat stat, List<NutritionDietRecord> diets) {
        Map<Integer, List<NutritionDietRecord>> dietRecords = diets.stream().filter(r -> null != r.getType()).collect(Collectors.groupingBy(NutritionDietRecord::getType));
        //早餐
        BigDecimal breakfastEnergy = BigDecimalUtil.convert(stat.getPlanEnergy().multiply(PERCENT_30));
        List<NutritionDietRecord> breakfastRecords = dietRecords.get(Constants.BREAKFAST);
        if (CollectionUtils.isEmpty(breakfastRecords)) {
            report.setBreakfastEnergy("0");
            report.setBreakfastEnergyPer("0");
            report.setBreakfastTips(report.getMealTips(breakfastEnergy, BigDecimal.ZERO));
        } else {
            BigDecimal totalTokenEnergy = BigDecimal.ZERO;
            for (NutritionDietRecord record : breakfastRecords) {
                if (record.getDishType() == Constants.SYS_DISH) {
                    FeignMessProdDto messProdDto = feignMessProductService.findDtoById(record.getMessProductId());
                    totalTokenEnergy = totalTokenEnergy.add(BigDecimalUtil.calculate(record.getDishWeight(), messProdDto != null ? messProdDto.getEnergy() : 0));
                } else {
                    totalTokenEnergy = totalTokenEnergy.add(BigDecimalUtil.calculate(record.getDishWeight(), record.getCustomDish().getNutrition().getEnergy()));
                }
            }
            report.setBreakfastEnergy(totalTokenEnergy.toString());
            report.setBreakfastEnergyPer(BigDecimalUtil.divide(totalTokenEnergy.multiply(HUNDRED), breakfastEnergy).toString());
            report.setBreakfastTips(report.getMealTips(breakfastEnergy, totalTokenEnergy));
        }

        //中餐
        BigDecimal lunchEnergy = BigDecimalUtil.convert(stat.getPlanEnergy().multiply(PERCENT_40));
        List<NutritionDietRecord> lunchRecords = dietRecords.get(Constants.LUNCH);
        if (CollectionUtils.isEmpty(lunchRecords)) {
            report.setLunchEnergy("0");
            report.setLunchEnergyPer("0");
            report.setLunchTips(report.getMealTips(lunchEnergy, BigDecimal.ZERO));
        } else {
            BigDecimal totalTokenEnergy = BigDecimal.ZERO;
            for (NutritionDietRecord record : lunchRecords) {
                if (record.getDishType() == Constants.SYS_DISH) {
                    FeignMessProdDto messProdDto = feignMessProductService.findDtoById(record.getMessProductId());
                    totalTokenEnergy = totalTokenEnergy.add(BigDecimalUtil.calculate(record.getDishWeight(), messProdDto != null ? messProdDto.getEnergy() : 0));
                } else {
                    totalTokenEnergy = totalTokenEnergy.add(BigDecimalUtil.calculate(record.getDishWeight(), record.getCustomDish().getNutrition().getEnergy()));
                }
            }
            report.setLunchEnergy(totalTokenEnergy.toString());
            report.setLunchEnergyPer(BigDecimalUtil.divide(totalTokenEnergy.multiply(HUNDRED), lunchEnergy).toString());
            report.setLunchTips(report.getMealTips(lunchEnergy, totalTokenEnergy));
        }

        //晚餐
        List<NutritionDietRecord> dinnerRecords = dietRecords.get(Constants.DINNER);
        if (CollectionUtils.isEmpty(dinnerRecords)) {
            report.setDinnerEnergyPer("0");
            report.setDinnerEnergy("0");
            report.setDinnerTips(report.getMealTips(breakfastEnergy, BigDecimal.ZERO));
        } else {
            BigDecimal totalTokenEnergy = BigDecimal.valueOf(0);
            for (NutritionDietRecord record : dinnerRecords) {
                if (record.getDishType() == Constants.SYS_DISH) {
                    FeignMessProdDto messProdDto = feignMessProductService.findDtoById(record.getMessProductId());
                    totalTokenEnergy = totalTokenEnergy.add(BigDecimalUtil.calculate(record.getDishWeight(), messProdDto != null ? messProdDto.getEnergy() : 0));
                } else {
                    totalTokenEnergy = totalTokenEnergy.add(BigDecimalUtil.calculate(record.getDishWeight(), record.getCustomDish().getNutrition().getEnergy()));
                }
            }
            report.setDinnerEnergy(totalTokenEnergy.toString());
            report.setDinnerEnergyPer(BigDecimalUtil.divide(totalTokenEnergy.multiply(HUNDRED), breakfastEnergy).toString());
            report.setDinnerTips(report.getMealTips(breakfastEnergy, totalTokenEnergy));
        }

        //加餐
    }

    /**
     * 营养元素
     *
     * @author loki
     * @date 2020/04/17 10:29
     */
    private void generateNutrition(NutritionDietReport report, NutritionIntakeStat stat) {
        //energy
        this.generateEnergyData(report, stat);

        //protein
        this.generateProteinData(report, stat);

        //fat
        this.generateFatData(report, stat);

        //carbs
        this.generateCarbsData(report, stat);

        //dietaryFiber
        this.generateDietaryFiberData(report, stat);
    }

    /**
     * energy
     *
     * @author loki
     * @date 2020/04/16 22:05
     */
    private void generateEnergyData(NutritionDietReport report, NutritionIntakeStat stat) {
        report.setIntakeEnergy(BigDecimalUtil.convert2Str(stat.getPlanEnergy()));
        report.setTokenEnergy(BigDecimalUtil.convert2Str(stat.getActualEnergy()));
        BigDecimal HUNDRED = new BigDecimal(100);
        BigDecimal score = BigDecimalUtil.divide(stat.getActualEnergy().multiply(HUNDRED), stat.getPlanEnergy());
        report.setEnergyScore(BigDecimalUtil.convert2Str(score));
        report.setEnergyPer(BigDecimalUtil.convert2Str(score));
        report.setEnergyTips(report.getEnergyTips());
    }

    /**
     * protein
     *
     * @author loki
     * @date 2020/04/16 22:05
     */
    private void generateProteinData(NutritionDietReport report, NutritionIntakeStat stat) {
        report.setIntakeProtein(BigDecimalUtil.convert2Str(stat.getPlanProtein()));
        report.setTokenProtein(BigDecimalUtil.convert2Str(stat.getActualProtein()));
        BigDecimal value = BigDecimalUtil.divide(stat.getActualProtein().multiply(HUNDRED), stat.getPlanProtein());
        report.setProteinPer(value.toString());
    }

    /**
     * fat
     *
     * @author loki
     * @date 2020/04/16 22:05
     */
    private void generateFatData(NutritionDietReport report, NutritionIntakeStat stat) {
        report.setIntakeFat(BigDecimalUtil.convert2Str(stat.getPlanFat()));
        report.setTokenFat(BigDecimalUtil.convert2Str(stat.getActualFat()));
        BigDecimal value = BigDecimalUtil.divide(stat.getActualFat().multiply(HUNDRED), stat.getPlanFat());
        report.setFatPer(value.toString());
    }

    /**
     * carbs
     *
     * @author loki
     * @date 2020/04/16 22:05
     */
    private void generateCarbsData(NutritionDietReport report, NutritionIntakeStat stat) {
        report.setIntakeCarbs(BigDecimalUtil.convert2Str(stat.getPlanCarbs()));
        report.setTokenCarbs(BigDecimalUtil.convert2Str(stat.getActualCarbs()));
        BigDecimal value = BigDecimalUtil.divide(stat.getActualCarbs().multiply(HUNDRED), stat.getPlanCarbs());
        report.setCarbsPer(value.toString());
    }

    /**
     * dietaryFiber
     *
     * @author loki
     * @date 2020/04/16 22:05
     */
    private void generateDietaryFiberData(NutritionDietReport report, NutritionIntakeStat stat) {
        report.setIntakeDietaryFiber(BigDecimalUtil.convert2Str(stat.getPlanDietaryFiber()));
        report.setTokenDietaryFiber(BigDecimalUtil.convert2Str(stat.getActualDietaryFiber()));
        BigDecimal value = BigDecimalUtil.divide(stat.getActualDietaryFiber().multiply(HUNDRED), stat.getPlanDietaryFiber());
        report.setDietaryFiberPer(value.toString());
    }

    public Object getReportDetail(LocalDate date) {
        Long empId = SecurityUtils.getUserId();

        NutritionDietReport report = dietReportRepository.queryByEmployeeIdAndReportDate(empId, date);
        NutritionDietReportDTO reportDTO = new NutritionDietReportDTO();
        if (null != report) {
            BeanUtil.copyProperties(report, reportDTO, Boolean.TRUE);
        }
        return reportDTO;
    }
}
