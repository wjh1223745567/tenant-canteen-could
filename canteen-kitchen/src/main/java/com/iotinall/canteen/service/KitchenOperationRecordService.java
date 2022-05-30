package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordAddReq;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordCriteria;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordDTO;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordEditReq;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenOperationRecord;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.KitchenItemRepository;
import com.iotinall.canteen.repository.KitchenOperationRecordRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * kitchen_operation_record ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-09 15:26:01
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KitchenOperationRecordService {
    @Resource
    private KitchenOperationRecordRepository kitchenOperationRecordRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private KitchenResultService kitchenResultService;

    public PageDTO<KitchenOperationRecordDTO> list(KitchenOperationRecordCriteria criteria, Pageable pageable) {
        Specification<KitchenOperationRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("itemType", criteria.getItemType()))
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .where(Criterion.eq("mealType", criteria.getMealType()))
                .whereByOr(
                        Criterion.like("dutyEmp.name", criteria.getKeywords()),
                        Criterion.like("auditor.name", criteria.getKeywords())
                )
                .build(true);
        Page<KitchenOperationRecord> page = kitchenOperationRecordRepository.findAll(spec, pageable);
        List<KitchenOperationRecordDTO> list = page.getContent().stream().map(item -> {
            KitchenOperationRecordDTO operationDTO = new KitchenOperationRecordDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, operationDTO);
            operationDTO.setAuditorId(item.getAuditorId());
            operationDTO.setAuditorName(item.getAuditorName());

            if (null != item.getDutyEmpId()) {
                FeignEmployeeDTO employee = feignEmployeeService.findById(item.getDutyEmpId());
                operationDTO.setDutyEmpId(employee.getId());
                operationDTO.setDutyEmpName(employee.getName());
                operationDTO.setDutyEmpAvatar(employee.getAvatar());
                operationDTO.setRole(employee.getPosition());
            }

            operationDTO.setItemId(item.getItem().getId());
            operationDTO.setItemName(item.getItem().getName());

            return operationDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(KitchenOperationRecordAddReq req) {
        KitchenOperationRecord record = new KitchenOperationRecord();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, record);

        KitchenItem item = kitchenItemRepository.findByIdIgnoreDeleted(req.getItemId());
        if (item == null) {
            throw new BizException("", "类型不存在");
        }
        record.setItem(item);
        record.setItemType(item.getGroupCode());

        FeignSimEmployeeDto employee = feignEmployeeService.findSimById(req.getDutyEmpId());
        if (null == employee) {
            throw new BizException("", "后厨用户不存在");
        }
        record.setDutyEmpId(employee.getId());

        record.setAuditorId(SecurityUtils.getUserId());

        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        record.setImg(req.getImg());
        kitchenOperationRecordRepository.save(record);
        kitchenResultService.addOrUpdate(record.getId(), record.getItemType(), record.getState(), record.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenOperationRecordEditReq req) {
        KitchenOperationRecord record = kitchenOperationRecordRepository.findById(req.getId())
                .orElseThrow(() -> new BizException("记录不存在"));

        BeanUtils.copyProperties(req, record);

        KitchenItem item = kitchenItemRepository.findByIdIgnoreDeleted(req.getItemId());
        if (item == null) {
            throw new BizException("", "类型不存在");
        }
        record.setItem(item);
        record.setItemType(item.getGroupCode());

        FeignSimEmployeeDto employee = feignEmployeeService.findSimById(req.getDutyEmpId());
        if (null == employee) {
            throw new BizException("", "后厨用户不存在");
        }
        record.setDutyEmpId(employee.getId());
        record.setAuditorId(SecurityUtils.getUserId());

        kitchenOperationRecordRepository.save(record);

        kitchenResultService.addOrUpdate(record.getId(), record.getItemType(), record.getState(), record.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        List<KitchenOperationRecord> recordList = new ArrayList<>();
        KitchenOperationRecord record;
        for (Long id : ids) {
            record = this.kitchenOperationRecordRepository.findById(id).orElseThrow(() -> new BizException("", "清洗/切配记录不存在"));
            kitchenOperationRecordRepository.delete(record);
            kitchenResultService.delByRecordIdAndItemType(id, record.getItemType());
        }
    }

    public BigDecimal todayPassRate() {
        List<KitchenOperationRecord> inspectRecords = this.kitchenOperationRecordRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }
}