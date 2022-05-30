package com.iotinall.canteen.service;

import cn.hutool.core.bean.BeanUtil;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.person.PersonAddReq;
import com.iotinall.canteen.dto.person.PersonRecordDTO;
import com.iotinall.canteen.dto.person.UpdateIntakeEnergyReq;
import com.iotinall.canteen.entity.NutritionPersonRecord;
import com.iotinall.canteen.dto.nutritionenc.FeignChronicDiseaseDto;
import com.iotinall.canteen.repository.NutritionPersonRecordRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户基本信息
 *
 * @author loki
 * @date 2020/04/10 14:33
 */
@Service
public class NutritionPersonRecordService {
    @Resource
    NutritionPersonRecordRepository nutritionPersonRecordRepository;
    @Resource
    private FeignChronicDiseaseService feignChronicDiseaseService;

    public void create(PersonAddReq req) {
        Long empId = SecurityUtils.getUserId();
        //用户营养档案
        NutritionPersonRecord personRecord = nutritionPersonRecordRepository.queryByEmployeeId(empId);
        if (null == personRecord) {
            personRecord = new NutritionPersonRecord();
            BeanUtils.copyProperties(req, personRecord);
            personRecord.setCreateTime(LocalDateTime.now());
            personRecord.setEmployeeId(empId);
        } else {
            BeanUtil.copyProperties(req, personRecord, false);
            personRecord.setUpdateTime(LocalDateTime.now());
        }
        this.nutritionPersonRecordRepository.save(personRecord);
    }

    public NutritionPersonRecord findPersonRecord() {
        return this.nutritionPersonRecordRepository.queryByEmployeeId(SecurityUtils.getUserId());
    }

    public PersonRecordDTO findPersonRecordDTO() {
        NutritionPersonRecord personRecord = this.findPersonRecord();
        if (null == personRecord) {
            return null;
        }

        PersonRecordDTO personRecordDTO = new PersonRecordDTO();
        BeanUtils.copyProperties(personRecord, personRecordDTO);

        //获取疾病中文名称
        if (!StringUtils.isBlank(personRecordDTO.getDisease())) {
            List<Long> diseaseIds = Arrays.asList(personRecordDTO.getDisease().split(";")).stream().map(Long::valueOf).collect(Collectors.toList());
            FeignChronicDiseaseDto disease = feignChronicDiseaseService.findDtoById(diseaseIds.get(0));
            personRecordDTO.setDiseaseName(disease.getName());
        }
        return personRecordDTO;
    }

    /**
     * 更新用户
     *
     * @author loki
     * @date 2020/12/08 14:17
     */
    public void updateIntakeEnergy(UpdateIntakeEnergyReq req) {
        NutritionPersonRecord personRecord = this.findPersonRecord();
        personRecord.setIntake(req.getIntake());
        this.nutritionPersonRecordRepository.save(personRecord);
    }

    public String findDisease(){
        NutritionPersonRecord personRecord = this.findPersonRecord();
        if (null == personRecord) {
            return null;
        }else{
            return personRecord.getDisease();
        }
    }
}
