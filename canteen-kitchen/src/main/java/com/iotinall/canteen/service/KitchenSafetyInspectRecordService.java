package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordAddReq;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordCriteria;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordDTO;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordEditReq;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenSafetyInspectRecord;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.KitchenItemRepository;
import com.iotinall.canteen.repository.KitchenSafetyInspectRecordRepository;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消防安全 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-10 11:10:12
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KitchenSafetyInspectRecordService {
    @Resource
    private KitchenSafetyInspectRecordRepository kitchenSafetyInspectRecordRepository;
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenResultService kitchenResultService;

    public PageDTO<KitchenSafetyInspectRecordDTO> list(KitchenSafetyInspectRecordCriteria criteria, Pageable pageable) {
        Specification<KitchenSafetyInspectRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("item.id", criteria.getItemId()))
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .whereByOr(
                        Criterion.like("dutyEmpName", criteria.getKeywords()),
                        Criterion.like("auditorName", criteria.getKeywords())
                )
                .build();
        Page<KitchenSafetyInspectRecord> page = kitchenSafetyInspectRecordRepository.findAll(spec, pageable);
        List<KitchenSafetyInspectRecordDTO> list = page.getContent().stream().map(item -> {
            KitchenSafetyInspectRecordDTO kitchenSafetyInspectRecordDTO = new KitchenSafetyInspectRecordDTO();

            BeanUtils.copyProperties(item, kitchenSafetyInspectRecordDTO);

            kitchenSafetyInspectRecordDTO.setAuditorId(item.getAuditorId());
            kitchenSafetyInspectRecordDTO.setAuditorName(item.getAuditorName());

            if (null != item.getDutyEmpId()) {
                FeignEmployeeDTO employee = feignEmployeeService.findById(item.getDutyEmpId());
                kitchenSafetyInspectRecordDTO.setDutyEmpId(employee.getId());
                kitchenSafetyInspectRecordDTO.setDutyEmpName(employee.getName());
                kitchenSafetyInspectRecordDTO.setDutyEmpAvatar(employee.getAvatar());
                kitchenSafetyInspectRecordDTO.setRole(employee.getPosition());
            }

            kitchenSafetyInspectRecordDTO.setItemId(item.getItem().getId());
            kitchenSafetyInspectRecordDTO.setItemName(item.getItem().getName());
            return kitchenSafetyInspectRecordDTO;
        }).collect(Collectors.toList());

        return PageUtil.toPageDTO(list, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(KitchenSafetyInspectRecordAddReq req) {
        KitchenSafetyInspectRecord record = new KitchenSafetyInspectRecord();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, record);

        FeignSimEmployeeDto byId = this.feignEmployeeService.findSimById(req.getDutyEmpId());
        if (null == byId) {
            throw new BizException("责任人不存在");
        }

        KitchenItem item = kitchenItemRepository.findById(req.getItemId()).orElse(null);
        if (item == null) {
            throw new BizException("", "检查项不能为空");
        }
        record.setItem(item);

        record.setAuditorId(SecurityUtils.getUserId());
        record.setAuditorName(SecurityUtils.getUserName());
        kitchenSafetyInspectRecordRepository.save(record);

        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_FIRE_PROTECT_ITEM, record.getState(), record.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenSafetyInspectRecordEditReq req) {
        KitchenSafetyInspectRecord record = kitchenSafetyInspectRecordRepository.findById(req.getId())
                .orElseThrow(() -> new BizException("record_not_exists", "记录不存在"));

        BeanUtils.copyProperties(req, record);

        FeignSimEmployeeDto byId = this.feignEmployeeService.findSimById(req.getDutyEmpId());
        if (null == byId) {
            throw new BizException("责任人不存在");
        }

        record.setDutyEmpId(byId.getId());
        record.setDutyEmpName(byId.getName());

        KitchenItem item = kitchenItemRepository.findById(req.getItemId())
                .orElseThrow(() -> new BizException("检查项不能为空"));
        record.setItem(item);

        record.setAuditorId(SecurityUtils.getUserId());
        record.setAuditorName(SecurityUtils.getUserName());
        kitchenSafetyInspectRecordRepository.save(record);

        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_FIRE_PROTECT_ITEM, record.getState(), record.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        kitchenSafetyInspectRecordRepository.deleteByIdIn(ids);
        kitchenResultService.batchDeleteByRecordId(ids, Constants.ITEM_GROUP_FIRE_PROTECT_ITEM);
    }

    public BigDecimal todayPassRate() {
        List<KitchenSafetyInspectRecord> inspectRecords = this.kitchenSafetyInspectRecordRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }
}