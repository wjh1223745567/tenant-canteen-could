package com.iotinall.canteen.service;

import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.Constants;
import com.iotinall.canteen.dto.diet.DietAddReq;
import com.iotinall.canteen.dto.diet.DietRecordDTO;
import com.iotinall.canteen.dto.diet.DietRecordResp;
import com.iotinall.canteen.dto.diet.NutritionDiffDTO;
import com.iotinall.canteen.entity.NutritionCustomDish;
import com.iotinall.canteen.entity.NutritionDietRecord;
import com.iotinall.canteen.entity.NutritionDietReport;
import com.iotinall.canteen.entity.NutritionIntakeStat;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.dto.nutritionenc.FeignDishDto;
import com.iotinall.canteen.repository.NutritionCustomDishRepository;
import com.iotinall.canteen.repository.NutritionDietRecordRepository;
import com.iotinall.canteen.repository.NutritionDietReportRepository;
import com.iotinall.canteen.repository.NutritionIntakeStatRepository;
import com.iotinall.canteen.utils.BigDecimalUtil;
import com.iotinall.canteen.utils.LocalDateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 营养档案,饮食记录
 *
 * @author loki
 * @date 2020/04/10 14:33
 */
@Service
public class NutritionDietRecordService {
    @Resource
    private NutritionDietRecordRepository dietRecordRepository;
    @Resource
    private NutritionDietReportRepository dietReportRepository;
    @Resource
    private FeignMessProductService feignMessProductService;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignDishService feignDishService;
    @Resource
    private NutritionCustomDishRepository customDishRepository;
    @Resource
    NutritionIntakeStatRepository intakeStatRepository;
    @Resource
    private NutritionCalculateBiz calculateBiz;

    /**
     * 每餐推荐能量占全天能量比例分配3:4:3
     *
     * @author loki
     * @date 2020/04/11 16:11
     */
    public Object findDietRecords(LocalDate date) {
        Long empId = SecurityUtils.getUserId();

        DietRecordResp resp = new DietRecordResp();
        List<NutritionDietRecord> records = this.dietRecordRepository.queryByEmployeeIdAndRecordDate(empId, LocalDateUtil.getQueryDate(date));
        this.genDietRecordList(records, resp);

        //获取计划于摄入的营养元素差值统计(从统计表中获取)
        NutritionDiffDTO nutrition = new NutritionDiffDTO();
        resp.setNutrition(nutrition);
        NutritionIntakeStat stat = intakeStatRepository.queryByEmployeeIdAndPlanDate(empId, date);
        if (null == stat) {
            NutritionDiffDTO nutritionDiffDTO = calculateBiz.calculateAll(empId, date);
            BeanUtils.copyProperties(nutritionDiffDTO, nutrition);
        } else {
            nutrition.setPlanEnergy(BigDecimalUtil.convert(stat.getPlanEnergy()));
            nutrition.setPlanProtein(BigDecimalUtil.convert(stat.getPlanProtein()));
            nutrition.setPlanFat(BigDecimalUtil.convert(stat.getPlanFat()));
            nutrition.setPlanCarbs(BigDecimalUtil.convert(stat.getPlanCarbs()));
            nutrition.setPlanDietaryFiber(BigDecimalUtil.convert(stat.getPlanDietaryFiber()));

            nutrition.setActualEnergy(BigDecimalUtil.convert(stat.getActualEnergy()));
            nutrition.setActualProtein(BigDecimalUtil.convert(stat.getActualProtein()));
            nutrition.setActualFat(BigDecimalUtil.convert(stat.getActualFat()));
            nutrition.setActualCarbs(BigDecimalUtil.convert(stat.getActualCarbs()));
            nutrition.setActualDietaryFiber(BigDecimalUtil.convert(stat.getActualDietaryFiber()));
        }

        /**
         * 推荐摄入能量需要加上运动消耗的能量
         */
//        nutrition.setPlanEnergy(calculateBiz.getIntakeEnergy(orgEmployee, nutrition.getPlanEnergy(), date));
        return resp;
    }

    /**
     * 组装数据
     *
     * @author loki
     * @date 2020/04/10 20:28
     */
    private List<DietRecordDTO> genDietRecord(List<NutritionDietRecord> records) {
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }
        List<DietRecordDTO> recordDTOList = new ArrayList<>();
        DietRecordDTO dietRecordDTO;
        for (NutritionDietRecord record : records) {
            dietRecordDTO = new DietRecordDTO();
            FeignMessProdDto feignMessProdDto = null;
            if(record.getMessProductId() != null){
                feignMessProdDto = feignMessProductService.findDtoById(record.getMessProductId());
            }

            if (record.getDishType() == Constants.SYS_DISH) {
                dietRecordDTO.setDishId(feignMessProdDto.getId());
                dietRecordDTO.setDishName(feignMessProdDto.getName());
                dietRecordDTO.setImg(feignMessProdDto.getImg());
                dietRecordDTO.setUnitEnergy(null == feignMessProdDto.getEnergy() ? BigDecimal.ZERO : new BigDecimal(feignMessProdDto.getEnergy()));
                dietRecordDTO.setTotalEnergy(BigDecimalUtil.calculate(record.getDishWeight(), feignMessProdDto.getEnergy()));
            } else {
                String dishId = record.getCustomDish().getDishId();
                FeignDishDto feignDishDto = feignDishService.findDtoById(dishId);

                String name = feignDishDto != null ? feignDishDto.getName() : feignMessProdDto != null ? feignMessProdDto.getName() : null;
                String img = feignDishDto != null ? feignDishDto.getImg() : feignMessProdDto != null ? feignMessProdDto.getImg() : null;
                dietRecordDTO.setDishId(record.getCustomDish().getId());
                dietRecordDTO.setDishName(name);
                dietRecordDTO.setImg(img);
                dietRecordDTO.setUnitEnergy(null == record.getCustomDish().getNutrition().getEnergy() ? BigDecimal.ZERO : new BigDecimal(record.getCustomDish().getNutrition().getEnergy()));
                dietRecordDTO.setTotalEnergy(BigDecimalUtil.calculate(record.getDishWeight(), record.getCustomDish().getNutrition().getEnergy()));
            }
            dietRecordDTO.setDishWeight(record.getDishWeight());
            dietRecordDTO.setDishType(record.getDishType());
            recordDTOList.add(dietRecordDTO);
        }
        return recordDTOList;
    }

    /**
     * 1.清空库中已存在的饮食记录
     * 2.保存最新的饮食记录
     * 3.统计最新的摄入营养元素并更新
     *
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object create(DietAddReq req) {
        Long empId = SecurityUtils.getUserId();

        //清空当天已经存在的饮食记录
        List<NutritionDietRecord> records = dietRecordRepository.queryByEmployeeIdAndRecordDate(empId, req.getDate());
        if (!CollectionUtils.isEmpty(records)) {
            this.dietRecordRepository.deleteAll(records);
        }

        //保存最新的饮食记录
        List<NutritionDietRecord> dietRecords = this.genDietRecords(empId, req);
        if (!CollectionUtils.isEmpty(dietRecords)) {
            this.dietRecordRepository.saveAll(dietRecords);
        } else {
            //清空已经统计的摄入营养元素
            NutritionIntakeStat stat = intakeStatRepository.queryByEmployeeIdAndPlanDate(empId, req.getDate());
            if (null != stat) {
                intakeStatRepository.delete(stat);
            }

            //清空已经生成的饮食报告
            NutritionDietReport report = dietReportRepository.queryByEmployeeIdAndReportDate(empId, req.getDate());
            if (null != report) {
                dietReportRepository.delete(report);
            }
        }

        //计算 每日摄入的能量/蛋白质/脂肪/碳水化合物/膳食纤维
        NutritionIntakeStat latestStat = intakeStatRepository.queryByEmployeeIdAndPlanDate(empId, req.getDate());
        NutritionDiffDTO nutrition;
        if (null == latestStat) {
            latestStat = new NutritionIntakeStat();
            latestStat.setPlanDate(req.getDate());
            latestStat.setEmployeeId(empId);

            //计算推荐摄入营养元素
            nutrition = calculateBiz.calculateAll(empId, req.getDate());
            latestStat.setPlanEnergy(nutrition.getPlanEnergy());
            latestStat.setPlanProtein(nutrition.getPlanProtein());
            latestStat.setPlanFat(nutrition.getPlanFat());
            latestStat.setPlanCarbs(nutrition.getPlanCarbs());
            latestStat.setPlanDietaryFiber(nutrition.getPlanDietaryFiber());
        } else {
            nutrition = new NutritionDiffDTO();
            nutrition.setPlanEnergy(BigDecimalUtil.convert(latestStat.getPlanEnergy()));
            nutrition.setPlanProtein(BigDecimalUtil.convert(latestStat.getPlanProtein()));
            nutrition.setPlanFat(BigDecimalUtil.convert(latestStat.getPlanFat()));
            nutrition.setPlanCarbs(BigDecimalUtil.convert(latestStat.getPlanCarbs()));
            nutrition.setPlanDietaryFiber(BigDecimalUtil.convert(latestStat.getPlanDietaryFiber()));
        }

        //计算已经摄入的营养元素
        NutritionDiffDTO tokenNutrition = calculateBiz.calculateNutritionToken(dietRecords);
        latestStat.setActualEnergy(tokenNutrition.getActualEnergy());
        latestStat.setActualProtein(tokenNutrition.getActualProtein());
        latestStat.setActualFat(tokenNutrition.getActualFat());
        latestStat.setActualCarbs(tokenNutrition.getActualCarbs());
        latestStat.setActualDietaryFiber(tokenNutrition.getActualDietaryFiber());
        this.intakeStatRepository.save(latestStat);

        nutrition.setActualEnergy(BigDecimalUtil.convert(latestStat.getActualEnergy()));
        nutrition.setActualProtein(BigDecimalUtil.convert(latestStat.getActualProtein()));
        nutrition.setActualFat(BigDecimalUtil.convert(latestStat.getActualFat()));
        nutrition.setActualDietaryFiber(BigDecimalUtil.convert(latestStat.getActualDietaryFiber()));
        nutrition.setActualCarbs(BigDecimalUtil.convert(latestStat.getActualCarbs()));

        DietRecordResp resp = new DietRecordResp();
        resp.setNutrition(nutrition);

        this.genDietRecordList(dietRecords, resp);
        return resp;
    }

    /**
     * 组装更新饮食记录
     *
     * @author loki
     * @date 2020/04/16 11:09
     */
    public List<NutritionDietRecord> genDietRecords(Long empId, DietAddReq req) {
        NutritionDietRecord record;
        List<NutritionDietRecord> dietRecords = new ArrayList<>(req.getDiets().size());
        for (DietRecordDTO recordDTO : req.getDiets()) {
            if (null == recordDTO) {
                continue;
            }
            record = new NutritionDietRecord();
            if (recordDTO.getDishType() == Constants.SYS_DISH) {
                FeignMessProdDto prodDto = feignMessProductService.findDtoById(recordDTO.getDishId());
                if (null == prodDto) {
                    continue;
                }
                record.setMessProductId(recordDTO.getDishId());
            } else {
                NutritionCustomDish customDish = customDishRepository.findById(recordDTO.getDishId()).orElse(null);
                if (null == customDish) {
                    continue;
                }
                record.setCustomDish(customDish);
            }
            record.setType(recordDTO.getType());
            record.setEmployeeId(empId);
            record.setDishType(recordDTO.getDishType());
            record.setDishWeight(recordDTO.getDishWeight());
            record.setRecordDate(req.getDate());
            dietRecords.add(record);
        }
        return dietRecords;
    }

    public Object calculate(DietAddReq req) {
        Long empId = SecurityUtils.getUserId();
        //计算推荐摄入的能量
        NutritionDiffDTO nutrition = calculateBiz.calculateAll(empId, req.getDate());
        DietRecordResp resp = new DietRecordResp();
        resp.setNutrition(nutrition);

        if (CollectionUtils.isEmpty(req.getDiets())) {
            return resp;
        }

        //处理饮食记录，这里处理的目的是为了代码复用，没什么特别之处
        List<NutritionDietRecord> dietRecords = this.genDietRecords(empId, req);

        //计算摄入营养元素
        NutritionDiffDTO tokenNutrition = calculateBiz.calculateNutritionToken(dietRecords);
        nutrition.setActualEnergy(tokenNutrition.getActualEnergy());
        nutrition.setActualProtein(tokenNutrition.getActualProtein());
        nutrition.setActualFat(tokenNutrition.getActualFat());
        nutrition.setActualCarbs(tokenNutrition.getActualCarbs());
        nutrition.setActualDietaryFiber(tokenNutrition.getActualDietaryFiber());

        this.genDietRecordList(dietRecords, resp);
        return resp;
    }

    /**
     * 处理饮食记录
     *
     * @author loki
     * @date 2020/04/21 15:16
     */
    private void genDietRecordList(List<NutritionDietRecord> dietRecords, DietRecordResp resp) {
        if (CollectionUtils.isEmpty(dietRecords)) {
            return;
        }
        Map<Integer, List<NutritionDietRecord>> recordMap = dietRecords.stream().filter(r -> null != r.getType()).collect(Collectors.groupingBy(NutritionDietRecord::getType));

        //早餐
        List<NutritionDietRecord> breakfastList = recordMap.get(Constants.BREAKFAST);
        resp.setBreakfast(this.genDietRecord(breakfastList));

        //中餐
        List<NutritionDietRecord> lunchList = recordMap.get(Constants.LUNCH);
        resp.setLunch(this.genDietRecord(lunchList));

        //晚餐
        List<NutritionDietRecord> dinnerList = recordMap.get(Constants.DINNER);
        resp.setDinner(this.genDietRecord(dinnerList));

        //加餐
        List<NutritionDietRecord> snackList = recordMap.get(Constants.SNACK);
        resp.setSnack(this.genDietRecord(snackList));
    }
}
