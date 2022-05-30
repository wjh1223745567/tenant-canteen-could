package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.assessrecord.AssessEnvspectListDto;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordAddReq;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordCriteria;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordDTO;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordEditReq;
import com.iotinall.canteen.dto.garbage.GarbageDutyEmpDTO;
import com.iotinall.canteen.dto.item.ItemDutyEmpDTO;
import com.iotinall.canteen.entity.KitchenEnvInspectRecord;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenItemEmployee;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.KitchenEnvInspectRecordRepository;
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
 * 环境卫生 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-10 13:48:40
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class KitchenEnvInspectRecordService {
    @Resource
    private KitchenEnvInspectRecordRepository kitchenEnvInspectRecordRepository;
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

    public PageDTO<KitchenEnvInspectRecordDTO> list(KitchenEnvInspectRecordCriteria criteria, Pageable pageable) {
        Specification<KitchenEnvInspectRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("item.id", criteria.getItemId()))
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .whereByOr(
                        Criterion.like("item.dutyEmployeeList.empName", criteria.getKeywords()),
                        Criterion.like("item.name", criteria.getKeywords())
                )
                .build(true);
        Page<KitchenEnvInspectRecord> page = kitchenEnvInspectRecordRepository.findAll(spec, pageable);
        List<KitchenEnvInspectRecordDTO> list = page.getContent().stream().map(item -> {
            KitchenEnvInspectRecordDTO envInspectRecordDTO = new KitchenEnvInspectRecordDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, envInspectRecordDTO);

            envInspectRecordDTO.setImg(item.getImg());

            if (null != item.getItem()) {
                envInspectRecordDTO.setItemId(item.getItem().getId());
                envInspectRecordDTO.setItemName(item.getItem().getName());
                envInspectRecordDTO.setItemDesc(item.getItem().getRequirements());

                if (!CollectionUtils.isEmpty(item.getItem().getDutyEmployeeList())) {
                    List<ItemDutyEmpDTO> envDutyEmpDTOS = new ArrayList<>();
                    ItemDutyEmpDTO envDutyEmpDTO;
                    FeignEmployeeDTO feignEmployeeDTO;
                    for (KitchenItemEmployee emp : item.getItem().getDutyEmployeeList()) {
                        feignEmployeeDTO = feignEmployeeService.findById(emp.getEmpId());
                        if (null != feignEmployeeDTO) {
                            envDutyEmpDTO = new ItemDutyEmpDTO();
                            envDutyEmpDTOS.add(envDutyEmpDTO);
                            envDutyEmpDTO.setDutyEmpId(emp.getEmpId());

                            envDutyEmpDTO.setDutyEmpName(feignEmployeeDTO.getName());
                            envDutyEmpDTO.setDutyAvatar(feignEmployeeDTO.getAvatar());
                        }
                    }
                    envInspectRecordDTO.setEnvDutyEmpDTOList(envDutyEmpDTOS);
                }
            }


            return envInspectRecordDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }


    /**
     * 环境卫生自动记录
     */
    @Transactional(rollbackFor = Exception.class)
    public String envRecordAdd(String key, String imgUrl, LocalDateTime localDateTime, String oldImg) {
        List<KitchenItem> kitchenItems = this.kitchenItemRepository.findByGroupCodeOrderBySeq(Constants.ITEM_GROUP_ENV_ITEM);
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

        KitchenEnvInspectRecord kitchenEnvInspectRecord;
        String[] checklist = kitchenItem.getCheckTime().split(",");
        for (String check : checklist) {
            LocalDateTime checkDateTime = LocalDateTimeUtil.str2LocalDateTime(localDateTime.toLocalDate() + " " + check, DateTimeFormatters.yyyyMMddHHmm);
            if (this.kitchenCameraImgService.timeCompare(checkDateTime, localDateTime)) {
                //TODO 获取照片
                kitchenEnvInspectRecord = this.kitchenEnvInspectRecordRepository.findByRecordTimeAndCameraIdAndItem(
                        checkDateTime,
                        camera.getId(),
                        kitchenItem);

                if (kitchenEnvInspectRecord == null) {
                    kitchenEnvInspectRecord = new KitchenEnvInspectRecord();
                }


                if (StringUtils.isBlank(oldImg)) {
                    kitchenEnvInspectRecord.setImg(kitchenCameraImgService.upCameraImg(imgUrl));
                } else {
                    kitchenEnvInspectRecord.setImg(oldImg);
                }

                kitchenEnvInspectRecord.setRecordTime(checkDateTime);
                kitchenEnvInspectRecord.setItem(kitchenItem);
                kitchenEnvInspectRecord.setState(1);
                kitchenEnvInspectRecord.setCreateTime(localDateTime);
                kitchenEnvInspectRecord.setCameraId(camera.getId());
                kitchenEnvInspectRecord.setComments(kitchenItem.getRequirements());
                kitchenEnvInspectRecord = this.kitchenEnvInspectRecordRepository.save(kitchenEnvInspectRecord);

                kitchenResultService.addOrUpdate(kitchenEnvInspectRecord.getId(), Constants.ITEM_GROUP_ENV_ITEM, kitchenEnvInspectRecord.getState(), kitchenEnvInspectRecord.getRecordTime());
                return kitchenEnvInspectRecord.getImg();
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object add(KitchenEnvInspectRecordAddReq req) {
        KitchenEnvInspectRecord envRecord = new KitchenEnvInspectRecord();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, envRecord);

//        OrgEmployee byId = this.employeeRepository.findById(req.getDutyEmpId()).orElseThrow(() -> new BizException("", "责任人不存在"));
//        envRecord.setDutyEmp(byId);

        KitchenItem item = kitchenItemRepository.findById(req.getItemId()).orElseThrow(() -> new BizException("", "检查项不能为空"));
        envRecord.setItem(item);

        kitchenEnvInspectRecordRepository.save(envRecord);
        kitchenResultService.addOrUpdate(envRecord.getId(), Constants.ITEM_GROUP_ENV_ITEM, envRecord.getState(), envRecord.getRecordTime());
        return envRecord;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenEnvInspectRecordEditReq req) {
        Optional<KitchenEnvInspectRecord> optional = kitchenEnvInspectRecordRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        KitchenEnvInspectRecord envRecord = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, envRecord);

        KitchenItem item = kitchenItemRepository.findById(req.getItemId()).orElseThrow(() -> new BizException("", "检查项不能为空"));
        envRecord.setItem(item);

        kitchenEnvInspectRecordRepository.save(envRecord);
        kitchenResultService.addOrUpdate(envRecord.getId(), Constants.ITEM_GROUP_ENV_ITEM, envRecord.getState(), envRecord.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        kitchenEnvInspectRecordRepository.deleteByIdIn(ids);
        kitchenResultService.batchDeleteByRecordId(ids, Constants.ITEM_GROUP_ENV_ITEM);
    }

    public BigDecimal todayPassRate() {
        List<KitchenEnvInspectRecord> inspectRecords = this.kitchenEnvInspectRecordRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }

    /**
     * 考核记录---环境卫生
     *
     * @param condition
     * @return
     */
    public List<AssessEnvspectListDto> envspectList(AssessRecordContentReq condition) {

        Specification<KitchenEnvInspectRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.gte("recordTime", LocalDateTime.of(condition.getBeginDate(), LocalTime.MIN)))
                .where(Criterion.lt("recordTime", LocalDateTime.of(condition.getEndDate(), LocalTime.MAX)))
                .where(Criterion.in("dutyEmployees.id", condition.getEmpIds()))
                .build();

        List<KitchenEnvInspectRecord> KitchenEnvInspectRecords = this.kitchenEnvInspectRecordRepository.findAll(spec);
        return KitchenEnvInspectRecordList(KitchenEnvInspectRecords);
    }

    public List<AssessEnvspectListDto> KitchenEnvInspectRecordList
            (List<KitchenEnvInspectRecord> KitchenEnvInspectRecords) {
        return KitchenEnvInspectRecords.stream().map(garbage -> {
            AssessEnvspectListDto assessEnvspectListDto = new AssessEnvspectListDto();
            assessEnvspectListDto.setId(garbage.getId());
            assessEnvspectListDto.setImg(garbage.getImg());
            assessEnvspectListDto.setRecordTime(garbage.getRecordTime());

            if (null != garbage.getItem()) {
                assessEnvspectListDto.setItemId(garbage.getItem().getId());
                assessEnvspectListDto.setItemName(garbage.getItem().getName());
                assessEnvspectListDto.setItemDesc(garbage.getItem().getRequirements());

                if (!CollectionUtils.isEmpty(garbage.getItem().getDutyEmployeeList())) {
                    FeignEmployeeDTO feignEmployeeDTO;
                    GarbageDutyEmpDTO garbageDutyEmpDTO;
                    List<GarbageDutyEmpDTO> garbageDutyEmpDTOS = new ArrayList<>();
                    for (KitchenItemEmployee emp : garbage.getItem().getDutyEmployeeList()) {
                        feignEmployeeDTO = feignEmployeeService.findById(emp.getEmpId());
                        if (null == feignEmployeeDTO) {
                            continue;
                        }
                        garbageDutyEmpDTO = new GarbageDutyEmpDTO();
                        garbageDutyEmpDTOS.add(garbageDutyEmpDTO);
                        garbageDutyEmpDTO.setDutyEmpId(emp.getEmpId());
                        garbageDutyEmpDTO.setDutyEmpName(feignEmployeeDTO.getName());
                        garbageDutyEmpDTO.setDutyAvatar(feignEmployeeDTO.getAvatar());
                        garbageDutyEmpDTO.setRole(feignEmployeeDTO.getPosition());
                    }
                    assessEnvspectListDto.setGarbageDutyEmpDTOList(garbageDutyEmpDTOS);
                }
            }
            return assessEnvspectListDto;
        }).collect(Collectors.toList());
    }
}