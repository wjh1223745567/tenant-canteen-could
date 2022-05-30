package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.assessrecord.AssessFacilityListDto;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.facilityduty.FacilityDutyEmpDTO;
import com.iotinall.canteen.dto.facilityrecord.KitchenFacilityRecordAddReq;
import com.iotinall.canteen.dto.facilityrecord.KitchenFacilityRecordCriteria;
import com.iotinall.canteen.dto.facilityrecord.KitchenFacilityRecordDTO;
import com.iotinall.canteen.dto.facilityrecord.KitchenFacilityRecordEditReq;
import com.iotinall.canteen.dto.garbage.GarbageDutyEmpDTO;
import com.iotinall.canteen.entity.KitchenFacilityRecord;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenItemEmployee;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.KitchenFacilityRecordRepository;
import com.iotinall.canteen.repository.KitchenItemRepository;
import com.iotinall.canteen.utils.DateTimeFormatters;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * kitchen_facility_record ServiceImpl
 * 设备检查记录
 *
 * @author xinbing
 * @date 2020-07-10 11:32:53
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class KitchenFacilityRecordService {
    @Resource
    private KitchenFacilityRecordRepository kitchenFacilityRecordRepository;
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenResultService kitchenResultService;
    @Resource
    private KitchenCameraImgService kitchenCameraImgService;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;

    public Object list(KitchenFacilityRecordCriteria criteria, Pageable pageable) {
        SpecificationBuilder spec = SpecificationBuilder.builder()
                .where(Criterion.eq("item.id", criteria.getItemId()))
                .where(Criterion.like("item.name", criteria.getItemName()))
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .whereByOr(
                        Criterion.like("dutyEmployees.name", criteria.getKeywords())
                        , Criterion.like("item.name", criteria.getKeywords())
                );
        Page<KitchenFacilityRecord> page = kitchenFacilityRecordRepository.findAll(spec.build(true), pageable);
        if (CollectionUtils.isEmpty(page.getContent())) {
            return PageUtil.toPageDTO(page);
        }

        List<KitchenFacilityRecord> kitchenFacilityRecordList = page.getContent();
        List<KitchenFacilityRecordDTO> list = new ArrayList<>();
        KitchenFacilityRecordDTO kitchenFacilityRecordDTO;
        for (KitchenFacilityRecord item : kitchenFacilityRecordList) {

            kitchenFacilityRecordDTO = new KitchenFacilityRecordDTO();
            list.add(kitchenFacilityRecordDTO);

            BeanUtils.copyProperties(item, kitchenFacilityRecordDTO);

            kitchenFacilityRecordDTO.setImg(item.getImg());

            if (item.getItem() != null) {
                kitchenFacilityRecordDTO.setItemId(item.getItem().getId());
                kitchenFacilityRecordDTO.setItemName(item.getItem().getName());
                kitchenFacilityRecordDTO.setItemDesc(item.getItem().getRequirements());

                if (!CollectionUtils.isEmpty(item.getItem().getDutyEmployeeList())) {
                    FeignEmployeeDTO employee;
                    FacilityDutyEmpDTO facilityDutyEmpDTO;
                    List<FacilityDutyEmpDTO> facilityDutyEmpDTOS = new ArrayList<>();
                    for (KitchenItemEmployee itemEmployee : item.getItem().getDutyEmployeeList()) {
                        employee = feignEmployeeService.findById(itemEmployee.getEmpId());
                        if (null == employee) {
                            continue;
                        }
                        facilityDutyEmpDTO = new FacilityDutyEmpDTO();
                        facilityDutyEmpDTOS.add(facilityDutyEmpDTO);
                        facilityDutyEmpDTO.setDutyEmpId(employee.getId());
                        facilityDutyEmpDTO.setDutyEmpName(employee.getName());
                        facilityDutyEmpDTO.setDutyAvatar(employee.getAvatar());
                    }
                    kitchenFacilityRecordDTO.setFacilityDutyEmpDTOList(facilityDutyEmpDTOS);
                }
            }
        }
        return PageUtil.toPageDTO(list, page);
    }


    /**
     * 设备设施自动记录
     */
    @Transactional(rollbackFor = Exception.class)
    public String facilityRecordAdd(String key, String imgUrl, LocalDateTime localDateTime, String oldImg) {
        List<KitchenItem> kitchenItems = this.kitchenItemRepository.findByGroupCodeOrderBySeq(Constants.ITEM_GROUP_FACILITY_ITEM);
        if (CollectionUtils.isEmpty(kitchenItems)) {
            return null;
        }
        KitchenItem kitchenItem = null;
        Map<Long, FeignCameraDto> cameraList;
        FeignCameraDto camera = null;
        for (KitchenItem item : kitchenItems) {
            if (StringUtils.isBlank(item.getCheckTime()) || StringUtils.isBlank(item.getCameraIds())) {
                continue;
            }

            String[] cameraIds = item.getCameraIds().split(",");
            cameraList = feignEquFaceDeviceService.findMapByIds(Arrays.stream(cameraIds).map(Long::valueOf).collect(Collectors.toSet()));
            for (Map.Entry<Long, FeignCameraDto> entry : cameraList.entrySet()) {
                if (entry.getValue().getDeviceNo().equals(key)) {
                    camera = entry.getValue();
                    kitchenItem = item;
                    break;
                }
            }
        }

        if (camera == null) {
            return oldImg;
        }

        KitchenFacilityRecord kitchenFacilityRecord;
        String[] result = kitchenItem.getCheckTime().split(",");
        for (String check : result) {
            if (StringUtils.isBlank(check)) {
                continue;
            }
            LocalDateTime checkDateTime = LocalDateTimeUtil.str2LocalDateTime(localDateTime.toLocalDate() + " " + check, DateTimeFormatters.yyyyMMddHHmm);
            if (this.kitchenCameraImgService.timeCompare(checkDateTime, localDateTime)) {
                //TODO 获取照片
                kitchenFacilityRecord = this.kitchenFacilityRecordRepository.findByRecordTimeAndCameraIdAndItem(checkDateTime, camera.getId(), kitchenItem);
                if (kitchenFacilityRecord == null) {
                    kitchenFacilityRecord = new KitchenFacilityRecord();
                }
                if (StringUtils.isBlank(oldImg)) {
                    kitchenFacilityRecord.setImg(kitchenCameraImgService.upCameraImg(imgUrl));
                } else {
                    kitchenFacilityRecord.setImg(oldImg);
                }
                kitchenFacilityRecord.setItem(kitchenItem);
                kitchenFacilityRecord.setRecordTime(checkDateTime);
                kitchenFacilityRecord.setState(1);
                kitchenFacilityRecord.setCreateTime(localDateTime);
                kitchenFacilityRecord.setCameraId(camera.getId());
                kitchenFacilityRecord.setRequirements(kitchenItem.getRequirements());
                kitchenFacilityRecord = this.kitchenFacilityRecordRepository.saveAndFlush(kitchenFacilityRecord);

                kitchenResultService.addOrUpdate(kitchenFacilityRecord.getId(), Constants.ITEM_GROUP_FACILITY_ITEM, kitchenFacilityRecord.getState(), kitchenFacilityRecord.getRecordTime());
                return kitchenFacilityRecord.getImg();
            }
        }
        return oldImg;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(KitchenFacilityRecordAddReq req) {
        KitchenFacilityRecord facilityRecord = new KitchenFacilityRecord();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, facilityRecord);

        KitchenItem item = kitchenItemRepository.findById(req.getItemId()).orElse(null);
        if (item == null) {
            throw new BizException("", "检查项不能为空");
        }
        facilityRecord.setItem(item);

        kitchenFacilityRecordRepository.save(facilityRecord);
        kitchenResultService.addOrUpdate(facilityRecord.getId(), Constants.ITEM_GROUP_FACILITY_ITEM, facilityRecord.getState(), facilityRecord.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenFacilityRecordEditReq req) {
        Optional<KitchenFacilityRecord> optional = kitchenFacilityRecordRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        KitchenFacilityRecord facilityRecord = optional.get();
        BeanUtils.copyProperties(req, facilityRecord);

        KitchenItem item = kitchenItemRepository.findById(req.getItemId()).orElse(null);
        if (item == null) {
            throw new BizException("", "检查项不能为空");
        }
        facilityRecord.setItem(item);

        kitchenFacilityRecordRepository.save(facilityRecord);
        kitchenResultService.addOrUpdate(facilityRecord.getId(), Constants.ITEM_GROUP_FACILITY_ITEM, facilityRecord.getState(), facilityRecord.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Optional<KitchenFacilityRecord> optional = kitchenFacilityRecordRepository.findById(id);
        if (!optional.isPresent()) {
            kitchenFacilityRecordRepository.deleteById(id);
            kitchenResultService.delByRecordIdAndItemType(id, Constants.ITEM_GROUP_FACILITY_ITEM);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        kitchenFacilityRecordRepository.deleteByIdIn(ids);
        kitchenResultService.batchDeleteByRecordId(ids, Constants.ITEM_GROUP_FACILITY_ITEM);
    }

    public BigDecimal todayPassRate() {
        List<KitchenFacilityRecord> inspectRecords = this.kitchenFacilityRecordRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }

    /**
     * 考核记录---设备设施
     *
     * @param condition
     * @return
     */
    public List<AssessFacilityListDto> facilityList(AssessRecordContentReq condition) {

        Specification<KitchenFacilityRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.gte("recordTime", LocalDateTime.of(condition.getBeginDate(), LocalTime.MIN)))
                .where(Criterion.lt("recordTime", LocalDateTime.of(condition.getEndDate(), LocalTime.MAX)))
                .where(Criterion.in("dutyEmployees.id", condition.getEmpIds()))
                .build();

        List<KitchenFacilityRecord> KitchenFacilityRecords = this.kitchenFacilityRecordRepository.findAll(spec);
        return KitchenFacilityRecordList(KitchenFacilityRecords);
    }

    public List<AssessFacilityListDto> KitchenFacilityRecordList(List<KitchenFacilityRecord> KitchenFacilityRecords) {
        return KitchenFacilityRecords.stream().map(garbage -> {

            AssessFacilityListDto assessFacilityListDto = new AssessFacilityListDto();
            assessFacilityListDto.setId(garbage.getId());
            assessFacilityListDto.setImg(garbage.getImg());
            assessFacilityListDto.setRecordTime(garbage.getRecordTime());

            if (null != garbage.getItem()) {
                assessFacilityListDto.setItemId(garbage.getItem().getId());
                assessFacilityListDto.setItemName(garbage.getItem().getName());
                assessFacilityListDto.setItemDesc(garbage.getItem().getRequirements());

                if (!CollectionUtils.isEmpty(garbage.getItem().getDutyEmployeeList())) {
                    FeignEmployeeDTO feignEmployeeDTO;
                    GarbageDutyEmpDTO garbageDutyEmpDTO;
                    List<GarbageDutyEmpDTO> garbageDutyEmpDTOS = new ArrayList<>();
                    for (KitchenItemEmployee itemEmployee : garbage.getItem().getDutyEmployeeList()) {
                        feignEmployeeDTO = feignEmployeeService.findById(itemEmployee.getEmpId());
                        if (null == feignEmployeeDTO) {
                            continue;
                        }
                        garbageDutyEmpDTO = new GarbageDutyEmpDTO();
                        garbageDutyEmpDTOS.add(garbageDutyEmpDTO);
                        garbageDutyEmpDTO.setDutyEmpId(feignEmployeeDTO.getId());
                        garbageDutyEmpDTO.setDutyEmpName(feignEmployeeDTO.getName());
                        garbageDutyEmpDTO.setDutyAvatar(feignEmployeeDTO.getAvatar());
                        garbageDutyEmpDTO.setRole(feignEmployeeDTO.getPosition());
                    }

                    assessFacilityListDto.setGarbageDutyEmpDTOList(garbageDutyEmpDTOS);
                }
            }
            return assessFacilityListDto;
        }).collect(Collectors.toList());
    }
}