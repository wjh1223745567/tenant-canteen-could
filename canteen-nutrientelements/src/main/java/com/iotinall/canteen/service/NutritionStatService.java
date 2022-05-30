package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.dto.diet.NutritionDiffDTO;
import com.iotinall.canteen.dto.stat.EnergyStatChartDTO;
import com.iotinall.canteen.dto.stat.EnergyStatDTO;
import com.iotinall.canteen.dto.stat.SportStatChartDTO;
import com.iotinall.canteen.dto.stat.StatureStatChartDTO;
import com.iotinall.canteen.entity.NutritionIntakeStat;
import com.iotinall.canteen.entity.NutritionPersonRecord;
import com.iotinall.canteen.entity.NutritionSportRecord;
import com.iotinall.canteen.entity.NutritionStatureRecord;
import com.iotinall.canteen.repository.NutritionIntakeStatRepository;
import com.iotinall.canteen.repository.NutritionPersonRecordRepository;
import com.iotinall.canteen.repository.NutritionSportRecordRepository;
import com.iotinall.canteen.repository.NutritionStatureRecordRepository;
import com.iotinall.canteen.utils.BigDecimalUtil;
import com.iotinall.canteen.utils.LocalDateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 营养档案,所有统计处理类
 *
 * @author loki
 * @date 2020/04/10 15:51
 */
@Service
public class NutritionStatService {
    @Resource
    private NutritionCalculateBiz calculateBiz;
    @Resource
    private NutritionIntakeStatRepository intakeStatRepository;
    @Resource
    private NutritionPersonRecordRepository nutritionPersonRecordRepository;
    @Resource
    private NutritionSportRecordRepository sportRecordRepository;
    @Resource
    private NutritionStatureRecordRepository statureRecordRepository;
    @Resource
    private NutritionStatureRecordService statureRecordService;

    public Object homepageEnergy() {
        LocalDate date = LocalDate.now();

        Long empId = SecurityUtils.getUserId();
        //计划
        NutritionPersonRecord personRecord = this.nutritionPersonRecordRepository.queryByEmployeeId(empId);
        BigDecimal plan = null == personRecord ? null : personRecord.getIntake();
        if (null == plan || BigDecimal.ZERO.compareTo(plan) == 0) {
            plan = this.calculateBiz.calculate(empId, date, Constants.CALCULATE_TYPE_ENERGY);
        }

        //实际
        NutritionDiffDTO nutritionToken = this.calculateBiz.calculateNutritionToken(empId, date);
        BigDecimal token = nutritionToken.getActualEnergy();

        return new EnergyStatDTO().setPlan(BigDecimalUtil.convert(plan)).setToken(BigDecimalUtil.convert(token)).setDiff(BigDecimalUtil.convert(token.subtract(plan)));
    }

    public Object statIntakeChart() {
        Long empId = SecurityUtils.getUserId();

        LocalDate endDate = LocalDate.now();
        LocalDate beginDate = endDate.minusDays(8);
        List<LocalDateUtil.BetweenDate> betweenDate = LocalDateUtil.getBetweenDate(beginDate, endDate);

        List<NutritionIntakeStat> result = intakeStatRepository.queryByEmployeeList(SecurityUtils.getUserId(), beginDate, endDate);
        List<EnergyStatChartDTO> energyStatList = new ArrayList<>(8);
        EnergyStatChartDTO energyStat;
        for (LocalDateUtil.BetweenDate date : betweenDate) {
            NutritionIntakeStat stat = result.stream().filter(item -> item.getPlanDate().equals(date.getDate())).findAny().orElse(null);
            energyStat = new EnergyStatChartDTO();
            if (null == stat) {
                /**
                 * 计算计划需要摄入的能量
                 */
                NutritionDiffDTO nutrition = calculateBiz.calculateAll(empId, date.getDate());
                NutritionIntakeStat intakeStat = new NutritionIntakeStat();
                BeanUtils.copyProperties(nutrition, intakeStat);
                intakeStat.setPlanDate(date.getDate());
                intakeStat.setEmployeeId(empId);
                this.intakeStatRepository.save(intakeStat);

                energyStat.setPlan(nutrition.getPlanEnergy());
                energyStat.setToken(BigDecimal.ZERO);
                energyStat.setDiff(BigDecimal.ZERO.subtract(nutrition.getPlanEnergy()));
            } else {
                energyStat.setPlan(stat.getPlanEnergy());
                energyStat.setToken(stat.getActualEnergy());
                energyStat.setDiff(stat.getActualEnergy().subtract(stat.getPlanEnergy()));
            }
            energyStat.setLabel(date.getLabel());
            energyStat.setDate(date.getDate().toString());
            energyStatList.add(energyStat);
        }
        return energyStatList;
    }

    public Object statIntakeList(Pageable pageable) {
        Long empId = SecurityUtils.getUserId();

        PageRequest pq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "planDate");
        Page<NutritionIntakeStat> pageResult = intakeStatRepository.queryByEmployeeId(empId, pq);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.emptyList(), pageResult);
        }

        List<EnergyStatChartDTO> result = pageResult.getContent().stream().map(item -> new EnergyStatChartDTO()
                .setPlan(item.getPlanEnergy())
                .setToken(item.getActualEnergy())
                .setDiff(item.getActualEnergy().subtract(item.getPlanEnergy()))
                .setDate(LocalDateUtil.format(item.getPlanDate()))
        ).collect(Collectors.toList());

        return PageUtil.toPageDTO(result, pageResult);
    }

    public Object statStatureChart(String code) {
        LocalDate endDate = LocalDate.now();
        LocalDate beginDate = endDate.minusDays(8);
        List<LocalDateUtil.BetweenDate> betweenDate = LocalDateUtil.getBetweenDate(beginDate, endDate);

        List<NutritionStatureRecord> result = this.statureRecordRepository.queryPersonStatureList(SecurityUtils.getUserId(), code, beginDate, endDate);
        List<StatureStatChartDTO> statureStatList = new ArrayList<>(8);
        StatureStatChartDTO statureStat;
        for (LocalDateUtil.BetweenDate date : betweenDate) {
            NutritionStatureRecord stat = result.stream().filter(item -> item.getRecordDate().equals(date.getDate())).findAny().orElse(null);
            statureStat = new StatureStatChartDTO();
            if (null == stat) {
                statureStat.setValue(BigDecimal.ZERO);
            } else {
                statureStat.setValue(stat.getValue());
                statureStat.setUnit(stat.getUnit());
            }
            statureStat.setLabel(date.getLabel());
            statureStat.setDate(LocalDateUtil.format(date.getDate()));
            statureStatList.add(statureStat);
        }
        return statureStatList;
    }

    public Object statStatureList(String type, Pageable pageable) {
        Long empId = SecurityUtils.getUserId();

        Page<NutritionStatureRecord> pageResult = this.statureRecordRepository.queryByEmployeeIdAndCodeOrderByRecordDateDesc(empId, type, pageable);

        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.emptyList(), pageResult);
        }
        List<StatureStatChartDTO> result = pageResult.getContent().stream().map(item -> {
            StatureStatChartDTO statChart = new StatureStatChartDTO();
            statChart.setDate(LocalDateUtil.format(item.getRecordDate()));
            statChart.setValue(item.getValue());
            statChart.setUnit(item.getUnit());
            return statChart;
        }).collect(Collectors.toList());

        return PageUtil.toPageDTO(result, pageResult);
    }

    public Object statConsumptionChart() {
        LocalDate endDate = LocalDate.now();
        LocalDate beginDate = endDate.minusDays(8);
        List<LocalDateUtil.BetweenDate> betweenDate = LocalDateUtil.getBetweenDate(beginDate, endDate);

        List<NutritionSportRecord> result = sportRecordRepository.queryByEmployeeList(SecurityUtils.getUserId(), beginDate, endDate);
        List<SportStatChartDTO> sportStatList = new ArrayList<>(8);
        SportStatChartDTO sportStat;
        //按日期分组
        Map<LocalDate, List<NutritionSportRecord>> sportGroupStat = result.stream().filter(r -> null != r.getSportDate()).collect(Collectors.groupingBy(NutritionSportRecord::getSportDate));
        for (LocalDateUtil.BetweenDate date : betweenDate) {
            List<NutritionSportRecord> stat = sportGroupStat.get(date.getDate());
            sportStat = new SportStatChartDTO();
            if (CollectionUtils.isEmpty(stat)) {
                sportStat.setBurnCalories(BigDecimal.ZERO);
            } else {
                sportStat.setSportName(stat.get(0).getSportName());
                sportStat.setBurnCalories(BigDecimalUtil.convert(stat.stream().map(NutritionSportRecord::getBurnCalories).mapToDouble(BigDecimal::doubleValue).sum()));
            }

            sportStat.setLabel(date.getLabel());
            sportStat.setDate(date.getDate().toString());
            sportStatList.add(sportStat);
        }
        return sportStatList;
    }

    public Object statConsumptionList(Pageable pageable) {
        Long empId = SecurityUtils.getUserId();

        Page<NutritionSportRecord> pageResult = sportRecordRepository.queryByEmployeeIdOrderBySportDateDesc(empId, pageable);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.emptyList(), pageResult);
        }

        List<SportStatChartDTO> result = pageResult.getContent().stream().map(item -> new SportStatChartDTO()
                .setDate(LocalDateUtil.format(item.getSportDate()))
                .setSportName(item.getSportName())
                .setBurnCalories(BigDecimalUtil.convert(item.getBurnCalories()))
        ).collect(Collectors.toList());

        return PageUtil.toPageDTO(result, pageResult);
    }
}
