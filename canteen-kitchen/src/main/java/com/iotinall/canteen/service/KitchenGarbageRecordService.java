package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.assessrecord.AssessGarbageListDto;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.garbage.*;
import com.iotinall.canteen.entity.KitchenGarbageRecord;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenItemEmployee;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.KitchenGarbageRecordRepository;
import com.iotinall.canteen.repository.KitchenItemRepository;
import com.iotinall.canteen.utils.DateTimeFormatters;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
 * 餐厨垃圾 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-10 11:46:09
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class KitchenGarbageRecordService {
    @Resource
    private KitchenGarbageRecordRepository kitchenGarbageRecordRepository;
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private KitchenResultService kitchenResultService;
    @Resource
    private KitchenCameraImgService kitchenCameraImgService;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;

    public PageDTO<KitchenGarbageRecordDTO> list(KitchenGarbageRecordCriteria criteria, Pageable pageable) {
        Specification<KitchenGarbageRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .whereByOr(
                        Criterion.like("item.dutyEmployeeList.empName", criteria.getKeywords()),
                        Criterion.like("item.name", criteria.getKeywords())
                )
                .build(true);
        Page<KitchenGarbageRecord> page = kitchenGarbageRecordRepository.findAll(spec, pageable);

        List<KitchenGarbageRecordDTO> list = page.getContent().stream().map(item -> {
            KitchenGarbageRecordDTO kitchenGarbageRecordDTO = new KitchenGarbageRecordDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, kitchenGarbageRecordDTO);
            kitchenGarbageRecordDTO.setImg(item.getImg());

            if (null != item.getItem()) {
                kitchenGarbageRecordDTO.setItemId(item.getItem().getId());
                kitchenGarbageRecordDTO.setItemName(item.getItem().getName());
                kitchenGarbageRecordDTO.setItemDesc(item.getItem().getRequirements());

                if (!CollectionUtils.isEmpty(item.getItem().getDutyEmployeeList())) {
                    GarbageDutyEmpDTO garbageDutyEmpDTO;
                    FeignEmployeeDTO employee;
                    List<GarbageDutyEmpDTO> garbageDutyEmpDTOS = new ArrayList<>();
                    for (KitchenItemEmployee itemEmployee : item.getItem().getDutyEmployeeList()) {
                        employee = feignEmployeeService.findById(itemEmployee.getEmpId());
                        if (null == employee) {
                            continue;
                        }

                        garbageDutyEmpDTO = new GarbageDutyEmpDTO();
                        garbageDutyEmpDTOS.add(garbageDutyEmpDTO);
                        garbageDutyEmpDTO.setDutyEmpId(employee.getId());
                        garbageDutyEmpDTO.setDutyEmpName(employee.getName());
                        garbageDutyEmpDTO.setDutyAvatar(employee.getAvatar());
                        garbageDutyEmpDTO.setRole(employee.getPosition());
                    }
                    kitchenGarbageRecordDTO.setGarbageDutyEmpDTOList(garbageDutyEmpDTOS);
                }
            }

            return kitchenGarbageRecordDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }


    /**
     * 厨余垃圾自动记录
     */
    @Transactional(rollbackFor = Exception.class)
    public String garbageRecordAdd(String key, String imgUrl, LocalDateTime recordDateTime, String oldImg) {
        List<KitchenItem> kitchenItems = this.kitchenItemRepository.findByGroupCodeOrderBySeq(Constants.ITEM_GROUP_KITCHEN_GARBAGE);
        if (CollectionUtils.isEmpty(kitchenItems)) {
            return oldImg;
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

        KitchenGarbageRecord kitchenGarbageRecord;
        String[] result = kitchenItem.getCheckTime().split(",");
        for (String check : result) {
            if (StringUtils.isBlank(check)) {
                continue;
            }
            LocalDateTime checkDateTime = LocalDateTimeUtil.str2LocalDateTime(recordDateTime.toLocalDate() + " " + check, DateTimeFormatters.yyyyMMddHHmm);
            if (this.kitchenCameraImgService.timeCompare(checkDateTime, recordDateTime)) {
                //TODO 获取图片
                kitchenGarbageRecord = this.kitchenGarbageRecordRepository.findByRecordTimeAndCameraIdAndItem(checkDateTime, camera.getId(), kitchenItem);
                if (null == kitchenGarbageRecord) {
                    kitchenGarbageRecord = new KitchenGarbageRecord();
                }
                if (StringUtils.isBlank(oldImg)) {
                    kitchenGarbageRecord.setImg(kitchenCameraImgService.upCameraImg(imgUrl));
                } else {
                    kitchenGarbageRecord.setImg(oldImg);
                }

                kitchenGarbageRecord.setComments(kitchenItem.getRequirements());
                kitchenGarbageRecord.setKitchenRegion(kitchenItem);
                kitchenGarbageRecord.setRecordTime(checkDateTime);
                kitchenGarbageRecord.setState(1);
                kitchenGarbageRecord.setCameraId(camera.getId());
                kitchenGarbageRecord.setItem(kitchenItem);
                kitchenGarbageRecord.setDeleted(Boolean.FALSE);
                kitchenGarbageRecord = this.kitchenGarbageRecordRepository.save(kitchenGarbageRecord);

                kitchenResultService.addOrUpdate(kitchenGarbageRecord.getId(), Constants.ITEM_GROUP_KITCHEN_GARBAGE, kitchenGarbageRecord.getState(), kitchenGarbageRecord.getRecordTime());
                return kitchenGarbageRecord.getImg();
            }
        }
        return oldImg;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(KitchenGarbageRecordAddReq req) {
        KitchenGarbageRecord garbageRecord = new KitchenGarbageRecord();
        BeanUtils.copyProperties(req, garbageRecord);

        kitchenGarbageRecordRepository.save(garbageRecord);

    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenGarbageRecordEditReq req) {
        Optional<KitchenGarbageRecord> optional = kitchenGarbageRecordRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        KitchenGarbageRecord garbageRecord = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, garbageRecord);
        kitchenGarbageRecordRepository.save(garbageRecord);
        kitchenResultService.addOrUpdate(garbageRecord.getId(), Constants.ITEM_GROUP_KITCHEN_GARBAGE, garbageRecord.getState(), garbageRecord.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }
        kitchenGarbageRecordRepository.deleteByIdIn(ids);
        kitchenResultService.batchDeleteByRecordId(ids, Constants.ITEM_GROUP_KITCHEN_GARBAGE);
    }

    public BigDecimal todayPassRate() {
        List<KitchenGarbageRecord> inspectRecords = this.kitchenGarbageRecordRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }

    /**
     * 考核记录---厨余垃圾
     *
     * @param condition
     * @return
     */
    public List<AssessGarbageListDto> garbageList(AssessRecordContentReq condition) {

        Specification<KitchenGarbageRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.gte("recordTime", LocalDateTime.of(condition.getBeginDate(), LocalTime.MIN)))
                .where(Criterion.lt("recordTime", LocalDateTime.of(condition.getEndDate(), LocalTime.MAX)))
                .where(Criterion.in("item.dutyEmployeeList.empId", condition.getEmpIds()))
                .build();

        List<KitchenGarbageRecord> kitchenAssessRecords = this.kitchenGarbageRecordRepository.findAll(spec);
        return kitchenGarbageRecordList(kitchenAssessRecords);
    }

    public List<AssessGarbageListDto> kitchenGarbageRecordList(List<KitchenGarbageRecord> kitchenGarbageRecords) {
        return kitchenGarbageRecords.stream().map(garbage -> {

            AssessGarbageListDto assessGarbageListDto = new AssessGarbageListDto();
            assessGarbageListDto.setId(garbage.getId());
            assessGarbageListDto.setImg(garbage.getImg());
            assessGarbageListDto.setRecordTime(garbage.getRecordTime());

            if (null != garbage.getItem()) {
                assessGarbageListDto.setItemId(garbage.getItem().getId());
                assessGarbageListDto.setItemName(garbage.getItem().getName());
                assessGarbageListDto.setItemDesc(garbage.getItem().getRequirements());

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

                    assessGarbageListDto.setGarbageDutyEmpDTOList(garbageDutyEmpDTOS);
                }
            }
            return assessGarbageListDto;
        }).collect(Collectors.toList());
    }
}