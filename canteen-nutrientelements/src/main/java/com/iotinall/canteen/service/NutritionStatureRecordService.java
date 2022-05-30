package com.iotinall.canteen.service;


import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.BodySizeEnum;
import com.iotinall.canteen.constants.BodyTypeEnum;
import com.iotinall.canteen.dto.sharingscale.StandardRemarkDto;
import com.iotinall.canteen.dto.stature.StatureAddReq;
import com.iotinall.canteen.dto.stature.StatureDTO;
import com.iotinall.canteen.entity.NutritionPersonRecord;
import com.iotinall.canteen.entity.NutritionStatureRecord;
import com.iotinall.canteen.dto.nutritionenc.FeignSysMaterialReq;
import com.iotinall.canteen.repository.NutritionStatureRecordRepository;
import com.iotinall.canteen.utils.LocalDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 营养档案,身材记录
 *
 * @author loki
 * @date 2020/04/10 14:33
 */
@Service
public class NutritionStatureRecordService {

    @Resource
    NutritionStatureRecordRepository statureRecordRepository;

    @Resource
    private NutritionPersonRecordService nutritionPersonRecordService;

    @Resource
    private FeignMaterialService feignMaterialService;

    public void create(StatureAddReq req) {
        Long empId = SecurityUtils.getUserId();

        NutritionStatureRecord record;
        List<NutritionStatureRecord> statureRecords = new ArrayList<>(req.getStatures().size());
        LocalDateTime optTime = LocalDateTime.now();
        int index = 0;
        for (StatureDTO stature : req.getStatures()) {
            if (null == stature) {
                continue;
            }
            record = new NutritionStatureRecord();
            record.setCode(stature.getCode());
            record.setRecordDate(req.getDate());
            record.setCreateTime(optTime);
            record.setName(stature.getName());
            record.setStandard(stature.getStandard());
            record.setValue(stature.getValue());
            record.setUnit(stature.getUnit());
            record.setSort(index);
            record.setEmployeeId(empId);
            statureRecords.add(record);
            index++;
        }
        this.statureRecordRepository.saveAll(statureRecords);
    }

    public void update(StatureAddReq req) {
        List<NutritionStatureRecord> statureRecords = this.find(LocalDate.now());
        this.statureRecordRepository.deleteAll(statureRecords);

        if (CollectionUtils.isEmpty(req.getStatures())) {
            return;
        }
        this.create(req);
    }

    public void updateAndCreate(StatureAddReq req){
        List<NutritionStatureRecord> statureRecords = this.find(LocalDate.now());
        if(!CollectionUtils.isEmpty(statureRecords)){
            this.statureRecordRepository.deleteAll(statureRecords);
        }

        if (CollectionUtils.isEmpty(req.getStatures())) {
            return;
        }
        this.create(req);
    }

    public List<NutritionStatureRecord> find(LocalDate date) {
        Long empId = SecurityUtils.getUserId();
        return statureRecordRepository.queryByEmployeeIdAndRecordDate(empId, LocalDateUtil.getQueryDate(date));
    }

    public Object findDTO(LocalDate date) {
        List<NutritionStatureRecord> statureRecords = this.find(date);
        if (CollectionUtils.isEmpty(statureRecords)) {
            return null;
        }

        StatureDTO statureDTO;
        List<StatureDTO> statureDTOList = new ArrayList<>(statureRecords.size());
        for (NutritionStatureRecord record : statureRecords) {
            if (null == record) {
                continue;
            }
            statureDTO = new StatureDTO();
            statureDTO.setCode(record.getCode());
            statureDTO.setName(record.getName());
            statureDTO.setStandard(record.getStandard());
            statureDTO.setUnit(record.getUnit());
            statureDTO.setValue(record.getValue());
            statureDTO.setCreateTime(record.getCreateTime());
            statureDTOList.add(statureDTO);
        }

        StatureDTO weight = statureDTOList.stream().filter(item -> Objects.equals(Integer.valueOf(item.getCode()), BodyTypeEnum.TYPE_WEIGHT.getCode())).findFirst().orElse(null);

        NutritionPersonRecord personRecord = nutritionPersonRecordService.findPersonRecord();

        if (personRecord == null) {
            throw new BizException("11", "未建立个人档案");
        }

        Integer nowage = (LocalDate.now().getYear() - personRecord.getBirthDate().getYear());

        for (StatureDTO dto : statureDTOList) {
            BodyTypeEnum typeEnum = BodyTypeEnum.findByCode(Integer.valueOf(dto.getCode()));

            StandardRemarkDto remarkDto = weight != null ? SharingScaleService.getStandard(typeEnum, dto.getValue(), personRecord.getPersonHeight().doubleValue(), personRecord.getGender(), weight.getValue().doubleValue(), nowage) : null;

            dto.setRemarkDtoList(remarkDto != null ? remarkDto.getRemarks() : Collections.emptyList());

            dto.getRemarkDtoList().stream().filter(item -> Objects.equals(item.getName(), dto.getStandard())).findFirst().ifPresent(item -> {
                dto.setRemark(item.getRemark());
            });

            dto.setScaleValue(remarkDto != null ? remarkDto.getScaleValue() : Collections.emptyList());
        }

        return statureDTOList;
    }

    public void delete(Long id){
        this.statureRecordRepository.deleteById(id);
    }


    /**
     * 元素是否适用
     *
     * @return
     */
    public Boolean isElementApplication(List<FeignSysMaterialReq> materials) {
        if (materials != null && !materials.isEmpty()) {
            //体重
            NutritionStatureRecord typeWeight = statureRecordRepository.findByTypeMaxDate(BodyTypeEnum.TYPE_WEIGHT.getCode().toString(), SecurityUtils.getUserId());

            //体脂率
            NutritionStatureRecord typeSubfat = statureRecordRepository.findByTypeMaxDate(BodyTypeEnum.TYPE_BODYFAT.getCode().toString(), SecurityUtils.getUserId());

            //体型
            NutritionStatureRecord typeBodyShape = statureRecordRepository.findByTypeMaxDate(BodyTypeEnum.TYPE_BODY_SHAPE.getCode().toString(), SecurityUtils.getUserId());

            //蛋白质
            NutritionStatureRecord protein = statureRecordRepository.findByTypeMaxDate(BodyTypeEnum.TYPE_PROTEIN.getCode().toString(), SecurityUtils.getUserId());

            List<String> ids = materials.stream().filter(item -> Objects.equals(item.getMaster(), 0)).map(FeignSysMaterialReq::getId).collect(Collectors.toList());

            List<NutritionNone> sysMaterials = this.feignMaterialService.findByIds(ids);
            for (NutritionNone sysMaterial : sysMaterials) {
                if (typeWeight != null && StringUtils.isNotBlank(typeWeight.getStandard())) {
                    switch (typeWeight.getStandard()) {
                        case "严重偏低":
                        case "偏低": {
                            if (sysMaterial.getProtein() != null && sysMaterial.getProtein() >= 12) {
                                return true;
                            }
                            break;
                        }
                        case "偏高": {
                            if (sysMaterial.getDietaryFiber() != null && sysMaterial.getDietaryFiber() >= 10) {
                                return true;
                            }
                            break;
                        }
                        case "严重偏高": {
                            if ((sysMaterial.getFat() != null && sysMaterial.getFat() >= 30) || (sysMaterial.getCholesterol() != null && sysMaterial.getCholesterol() >= 200)) {
                                return false;
                            }
                            if ((sysMaterial.getDietaryFiber() != null && sysMaterial.getDietaryFiber() >= 10) || (sysMaterial.getVc() != null && sysMaterial.getVc() >= 20)) {
                                return true;
                            }
                            break;
                        }
                    }
                }
            }

            for (NutritionNone sysMaterial : sysMaterials) {
                if (typeSubfat != null && StringUtils.isNotBlank(typeSubfat.getStandard())) {
                    switch (typeSubfat.getStandard()) {
                        case "偏低": {
                            if (sysMaterial.getProtein() != null && sysMaterial.getProtein() >= 12) {
                                return true;
                            }
                            break;
                        }
                        case "偏高": {
                            if ((sysMaterial.getFat() != null && sysMaterial.getFat() >= 30) || (sysMaterial.getCholesterol() != null && sysMaterial.getCholesterol() >= 200)) {
                                return false;
                            }
                            break;
                        }
                    }
                }
            }

            for (NutritionNone sysMaterial : sysMaterials) {
                if (typeBodyShape != null && typeBodyShape.getValue() != null) {
                    switch (BodySizeEnum.findByCode(typeBodyShape.getValue().intValue())) {
                        //偏瘦型
                        case thin: {
                            if (sysMaterial.getProtein() != null && sysMaterial.getProtein() >= 12) {
                                return true;
                            }
                            break;
                        }
                        //肥胖型  偏胖型
                        case obese:
                        case overweight: {
                            if (sysMaterial.getFat() != null && sysMaterial.getFat() >= 30) {
                                return false;
                            }
                            break;
                        }

                    }
                }
            }

            for (NutritionNone sysMaterial : sysMaterials) {
                if (protein != null && StringUtils.isNotBlank(protein.getStandard()) && Objects.equals("偏低", protein.getStandard())) {
                    //偏低
                    if (sysMaterial.getProtein() != null && sysMaterial.getProtein() >= 12) {
                        return true;
                    }
                }
            }
        }
        return null;
    }
}
