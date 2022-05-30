package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordAddReq;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordCriteria;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordDTO;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordEditReq;
import com.iotinall.canteen.entity.KitchenFoodAdditiveRecord;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.KitchenFoodAdditiveRecordRepository;
import com.iotinall.canteen.repository.KitchenItemRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 添加剂记录 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-10 14:24:45
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KitchenFoodAdditiveRecordService {
    @Resource
    private KitchenFoodAdditiveRecordRepository kitchenFoodAdditiveRecordRepository;
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenResultService kitchenResultService;
    @Resource
    private FeignMessProductService feignMessProductService;

    public PageDTO<KitchenFoodAdditiveRecordDTO> list(KitchenFoodAdditiveRecordCriteria criteria, Pageable pageable) {
        Specification<KitchenFoodAdditiveRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .where(Criterion.eq("state", criteria.getState()))
                .whereByOr(
                        Criterion.like("item.name", criteria.getKeywords()),
                        Criterion.like("messProductName", criteria.getKeywords())
                )
                .build(true);
        Page<KitchenFoodAdditiveRecord> page = kitchenFoodAdditiveRecordRepository.findAll(spec, pageable);
        List<KitchenFoodAdditiveRecordDTO> list = page.getContent().stream().map(item -> {
            KitchenFoodAdditiveRecordDTO kitchenFoodAdditiveRecordDTO = new KitchenFoodAdditiveRecordDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, kitchenFoodAdditiveRecordDTO);

            kitchenFoodAdditiveRecordDTO.setProductId(item.getMessProductId());
            kitchenFoodAdditiveRecordDTO.setProductName(item.getMessProductName());

            if (null != item.getItem()) {
                kitchenFoodAdditiveRecordDTO.setItemId(item.getItem().getId());
                kitchenFoodAdditiveRecordDTO.setItemName(item.getItem().getName());
            }

            if (null != item.getAuditorId()) {
                FeignSimEmployeeDto employeeDto = feignEmployeeService.findSimById(item.getAuditorId());
                kitchenFoodAdditiveRecordDTO.setAuditorId(item.getAuditorId());
                kitchenFoodAdditiveRecordDTO.setAuditorName(employeeDto.getName());
            }

            if (null != item.getDutyEmpId()) {
                FeignEmployeeDTO employeeDto = feignEmployeeService.findById(item.getDutyEmpId());
                kitchenFoodAdditiveRecordDTO.setDutyEmpId(employeeDto.getId());
                kitchenFoodAdditiveRecordDTO.setDutyEmpName(employeeDto.getName());
                kitchenFoodAdditiveRecordDTO.setDutyEmpAvatar(employeeDto.getAvatar());
                kitchenFoodAdditiveRecordDTO.setRole(employeeDto.getPosition());
            }

            return kitchenFoodAdditiveRecordDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }


    @Transactional(rollbackFor = Exception.class)
    public void add(KitchenFoodAdditiveRecordAddReq req) {
        KitchenFoodAdditiveRecord additiveRecord = new KitchenFoodAdditiveRecord();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, additiveRecord);
        additiveRecord.setAuditorId(SecurityUtils.getUserId());

        FeignSimEmployeeDto employeeDto = feignEmployeeService.findSimById(req.getDutyEmpId());
        if (null == employeeDto) {
            throw new BizException("", "责任人不存在");
        }
        additiveRecord.setDutyEmpId(employeeDto.getId());

        KitchenItem item = kitchenItemRepository.findById(req.getItemId()).orElseThrow(
                () -> new BizException("检查项不存在")
        );

        additiveRecord.setItem(item);

        FeignMessProdDto messProdDto = feignMessProductService.findDtoById(req.getProductId());
        if (null == messProdDto) {
            throw new BizException("菜品不存在");
        }

        additiveRecord.setMessProductId(messProdDto.getId());
        additiveRecord.setMessProductName(messProdDto.getName());

        kitchenFoodAdditiveRecordRepository.save(additiveRecord);
        kitchenResultService.addOrUpdate(additiveRecord.getId(), Constants.ITEM_GROUP_FOOD_ADDITIVES, additiveRecord.getState(), additiveRecord.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(KitchenFoodAdditiveRecordEditReq req) {
        Optional<KitchenFoodAdditiveRecord> optional = kitchenFoodAdditiveRecordRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        KitchenFoodAdditiveRecord additiveRecord = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, additiveRecord);
        additiveRecord.setAuditorId(SecurityUtils.getUserId());

        FeignSimEmployeeDto employeeDto = feignEmployeeService.findSimById(req.getDutyEmpId());
        if (null == employeeDto) {
            throw new BizException("", "责任人不存在");
        }
        additiveRecord.setDutyEmpId(employeeDto.getId());

        KitchenItem item = kitchenItemRepository.findById(req.getItemId()).orElseThrow(
                () -> new BizException("检查项不存在")
        );
        additiveRecord.setItem(item);

        FeignMessProdDto messProdDto = feignMessProductService.findDtoById(req.getProductId());
        if (null == messProdDto) {
            throw new BizException("菜品不存在");
        }

        additiveRecord.setMessProductId(messProdDto.getId());
        additiveRecord.setMessProductName(messProdDto.getName());

        kitchenFoodAdditiveRecordRepository.save(additiveRecord);
        kitchenResultService.addOrUpdate(additiveRecord.getId(), Constants.ITEM_GROUP_FOOD_ADDITIVES, additiveRecord.getState(), additiveRecord.getRecordTime());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Optional<KitchenFoodAdditiveRecord> optional = kitchenFoodAdditiveRecordRepository.findById(id);
        if (!optional.isPresent()) {
            kitchenFoodAdditiveRecordRepository.deleteById(id);
            kitchenResultService.delByRecordIdAndItemType(id, Constants.ITEM_GROUP_FOOD_ADDITIVES);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }


        kitchenFoodAdditiveRecordRepository.deleteByIdIn(ids);
        kitchenResultService.batchDeleteByRecordId(ids, Constants.ITEM_GROUP_FOOD_ADDITIVES);
    }

    public BigDecimal todayPassRate() {
        List<KitchenFoodAdditiveRecord> inspectRecords = this.kitchenFoodAdditiveRecordRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }
}