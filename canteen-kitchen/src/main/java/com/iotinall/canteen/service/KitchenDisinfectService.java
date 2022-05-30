package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.assessrecord.AssessDisinfectListDto;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.disinfect.*;
import com.iotinall.canteen.entity.KitchenDisinfect;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenItemEmployee;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.KitchenDisinfectRepository;
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
 * 消毒管理 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-06 15:32:49
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class KitchenDisinfectService {
    @Resource
    private KitchenDisinfectRepository kitchenDisinfectRepository;
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenResultService kitchenResultService;
    @Resource
    private KitchenCameraImgService kitchenCameraImgService;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;

    public PageDTO<KitchenDisinfectDTO> list(KitchenDisinfectCriteria criteria, Pageable pageable) {
        Specification<KitchenDisinfect> spec = SpecificationBuilder.builder()
                .fetch("item")
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.gte("recordTime", criteria.getBeginDisinfectTime() == null ? null : criteria.getBeginDisinfectTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndDisinfectTime() == null ? null : criteria.getEndDisinfectTime().atTime(LocalTime.MAX)))
                .whereByOr(
                        Criterion.like("item.name", criteria.getKeywords())
                )
                .build(true);
        Page<KitchenDisinfect> page = kitchenDisinfectRepository.findAll(spec, pageable);
        List<KitchenDisinfectDTO> list = page.getContent().stream().map(this::toKitchenDisinfectDTO).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    public KitchenDisinfectDTO toKitchenDisinfectDTO(KitchenDisinfect disinfect) {
        KitchenDisinfectDTO disinfectDTO = new KitchenDisinfectDTO();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(disinfect, disinfectDTO);
        disinfectDTO.setImg(disinfect.getImg());

        if (null != disinfect.getItem()) {
            disinfectDTO.setItemId(disinfect.getItem().getId());
            disinfectDTO.setItemName(disinfect.getItem().getName());
            disinfectDTO.setRequirements(disinfect.getItem().getRequirements());

            if (!CollectionUtils.isEmpty(disinfect.getItem().getDutyEmployeeList())) {
                KitchenDisinfectDutyEmpDTO kitchenDisinfectDutyEmpDTO;
                List<KitchenDisinfectDutyEmpDTO> kitchenDisinfectDutyEmpDTOS = new ArrayList<>();
                FeignEmployeeDTO employee;
                for (KitchenItemEmployee itemEmployee : disinfect.getItem().getDutyEmployeeList()) {
                    employee = feignEmployeeService.findById(itemEmployee.getEmpId());
                    if (null == employee) {
                        continue;
                    }

                    kitchenDisinfectDutyEmpDTO = new KitchenDisinfectDutyEmpDTO();
                    kitchenDisinfectDutyEmpDTOS.add(kitchenDisinfectDutyEmpDTO);
                    kitchenDisinfectDutyEmpDTO.setDutyEmpId(employee.getId());
                    kitchenDisinfectDutyEmpDTO.setDutyEmpName(employee.getName());
                    kitchenDisinfectDutyEmpDTO.setDutyAvatar(employee.getAvatar());
                    kitchenDisinfectDutyEmpDTO.setRole(employee.getPosition());
                }
                disinfectDTO.setKitchenDisinfectDutyEmpDTOS(kitchenDisinfectDutyEmpDTOS);
            }
        }
        return disinfectDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(KitchenDisinfectAddReq req) {
        KitchenDisinfect disnfect = new KitchenDisinfect();
        KitchenItem item = kitchenItemRepository.findByIdIgnoreDeleted(req.getItemId());
        if (item == null) {
            throw new BizException("", "消毒项不存在");
        }

        BeanUtils.copyProperties(req, disnfect);
        disnfect.setItem(item);

        FeignEmployeeDTO auditor = this.feignEmployeeService.findById(SecurityUtils.getUserId());
        if (auditor == null) {
            throw new BizException("", "用户不存在");
        }

        disnfect.setAuditorId(auditor.getId());

        kitchenDisinfectRepository.save(disnfect);
        kitchenResultService.addOrUpdate(disnfect.getId(), Constants.ITEM_GROUP_DISINFECT_ITEM, disnfect.getState(), disnfect.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenDisinfectEditReq req) {
        Optional<KitchenDisinfect> optional = kitchenDisinfectRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        KitchenDisinfect disnfect = optional.get();

        KitchenItem item = kitchenItemRepository.findByIdIgnoreDeleted(req.getItemId());
        if (item == null) {
            throw new BizException("", "消毒项不存在");
        }


        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, disnfect);

        FeignEmployeeDTO auditor = this.feignEmployeeService.findById(SecurityUtils.getUserId());
        if (auditor == null) {
            throw new BizException("", "用户不存在");
        }
        disnfect.setAuditorId(auditor.getId());
        disnfect.setItem(item);

        kitchenDisinfectRepository.save(disnfect);
        kitchenResultService.addOrUpdate(disnfect.getId(), Constants.ITEM_GROUP_DISINFECT_ITEM, disnfect.getState(), disnfect.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        kitchenDisinfectRepository.deleteByIdIn(ids);
        kitchenResultService.batchDeleteByRecordId(ids, Constants.ITEM_GROUP_DISINFECT_ITEM);
    }

    /**
     * 查询当天合格率
     *
     * @return
     */
    public BigDecimal todayPassRate() {
        List<KitchenDisinfect> inspectRecords = this.kitchenDisinfectRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }

    /**
     * 消毒记录自动记录
     */
    @Transactional(rollbackFor = Exception.class)
    public String disinfectRecordAdd(String key, String imgUrl, LocalDateTime localDateTime, String oldImg) {
        List<KitchenItem> kitchenItems = this.kitchenItemRepository.findByGroupCodeOrderBySeq(Constants.ITEM_GROUP_DISINFECT_ITEM);
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

        KitchenDisinfect kitchenDisinfect;
        String[] result = kitchenItem.getCheckTime().split(",");
        for (String check : result) {
            if (StringUtils.isBlank(check)) {
                continue;
            }
            LocalDateTime checkDateTime = LocalDateTimeUtil.str2LocalDateTime(localDateTime.toLocalDate() + " " + check, DateTimeFormatters.yyyyMMddHHmm);
            if (this.kitchenCameraImgService.timeCompare(checkDateTime, localDateTime)) {
                //TODO 获取照片
                kitchenDisinfect = this.kitchenDisinfectRepository.findByRecordTimeAndCameraIdAndItem(checkDateTime, camera.getId(), kitchenItem);
                if (null == kitchenDisinfect) {
                    kitchenDisinfect = new KitchenDisinfect();
                }
                if (StringUtils.isBlank(oldImg)) {
                    kitchenDisinfect.setImg(kitchenCameraImgService.upCameraImg(imgUrl));
                } else {
                    kitchenDisinfect.setImg(oldImg);
                }

                kitchenDisinfect.setRecordTime(checkDateTime);
                kitchenDisinfect.setItem(kitchenItem);
                kitchenDisinfect.setState(1);
                kitchenDisinfect.setCameraId(camera.getId());
                kitchenDisinfect.setRequirements(kitchenItem.getRequirements());
                kitchenDisinfect = this.kitchenDisinfectRepository.save(kitchenDisinfect);

                kitchenResultService.addOrUpdate(kitchenDisinfect.getId(), Constants.ITEM_GROUP_DISINFECT_ITEM, kitchenDisinfect.getState(), kitchenDisinfect.getRecordTime());
                return kitchenDisinfect.getImg();
            }
        }
        return oldImg;
    }

    /**
     * 消毒管理
     *
     * @param condition
     * @return
     */
    public List<AssessDisinfectListDto> disinfectList(AssessRecordContentReq condition) {

        Specification<KitchenDisinfect> spec = SpecificationBuilder.builder()
                .where(Criterion.gte("recordTime", LocalDateTime.of(condition.getBeginDate(), LocalTime.MIN)))
                .where(Criterion.lt("recordTime", LocalDateTime.of(condition.getEndDate(), LocalTime.MAX)))
                .where(Criterion.in("item.dutyEmployeeList.empId", condition.getEmpIds()))
                .build();

        List<KitchenDisinfect> kitchenDisinfects = this.kitchenDisinfectRepository.findAll(spec);
        return kitchenDisinfects.stream().map(disinfect -> {
            AssessDisinfectListDto assessDisinfectListDto = new AssessDisinfectListDto();
            assessDisinfectListDto.setId(disinfect.getId());
            assessDisinfectListDto.setRequirements(disinfect.getItem() != null ? disinfect.getItem().getRequirements() : null);
            assessDisinfectListDto.setImg(disinfect.getImg());
            assessDisinfectListDto.setRecordTime(disinfect.getRecordTime());
            assessDisinfectListDto.setComments(disinfect.getComments());
            assessDisinfectListDto.setState(disinfect.getState());

            if (null != disinfect.getItem()) {
                assessDisinfectListDto.setItemId(disinfect.getItem().getId());
                assessDisinfectListDto.setItemName(disinfect.getItem().getName());
                if (!CollectionUtils.isEmpty(disinfect.getItem().getDutyEmployeeList())) {
                    FeignEmployeeDTO feignEmployeeDTO;
                    KitchenDisinfectDutyEmpDTO kitchenDisinfectDutyEmpDTO;
                    List<KitchenDisinfectDutyEmpDTO> kitchenDisinfectDutyEmpDTOS = new ArrayList<>();
                    for (KitchenItemEmployee itemEmployee : disinfect.getItem().getDutyEmployeeList()) {
                        feignEmployeeDTO = feignEmployeeService.findById(itemEmployee.getEmpId());
                        if (null == feignEmployeeDTO) {
                            continue;
                        }

                        kitchenDisinfectDutyEmpDTO = new KitchenDisinfectDutyEmpDTO();
                        kitchenDisinfectDutyEmpDTOS.add(kitchenDisinfectDutyEmpDTO);

                        kitchenDisinfectDutyEmpDTO.setDutyEmpId(feignEmployeeDTO.getId());
                        kitchenDisinfectDutyEmpDTO.setDutyEmpName(feignEmployeeDTO.getName());
                        kitchenDisinfectDutyEmpDTO.setDutyAvatar(feignEmployeeDTO.getAvatar());
                        kitchenDisinfectDutyEmpDTO.setRole(feignEmployeeDTO.getPosition());
                    }
                    assessDisinfectListDto.setKitchenDisinfectDutyEmpDTOS(kitchenDisinfectDutyEmpDTOS);
                }
            }

            return assessDisinfectListDto;

        }).collect(Collectors.toList());
    }
}