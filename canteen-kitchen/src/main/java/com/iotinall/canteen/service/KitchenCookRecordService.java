package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.cookrecord.*;
import com.iotinall.canteen.entity.KitchenCookRecord;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.KitchenCookRecordRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class KitchenCookRecordService {
    @Resource
    private KitchenCookRecordRepository kitchenCookRecordRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignMessProductService feignMessProductService;
    @Resource
    private KitchenResultService kitchenResultService;
    @PersistenceContext
    private EntityManager entityManager;

    public PageDTO<CookRecordDTO> list(CookRecordCriteria criteria, Pageable pageable) {
        Specification<KitchenCookRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("mealType", criteria.getMealType()))
                .where(Criterion.gte("recordTime", criteria.getBeginRecordTime() == null ? null : criteria.getBeginRecordTime().atStartOfDay()))
                .where(Criterion.lt("recordTime", criteria.getEndRecordTime() == null ? null : criteria.getEndRecordTime().atTime(LocalTime.MAX)))
                .where(Criterion.eq("state", criteria.getState()))
                .whereByOr(
                        Criterion.like("dutyEmpName", criteria.getKeywords()),
                        Criterion.like("messProductName", criteria.getKeywords())
                )
                .build();
        Page<KitchenCookRecord> page = kitchenCookRecordRepository.findAll(spec, pageable);
        Map<String, CommentsDTO> cache = new HashMap<>(pageable.getPageSize());

        List<CookRecordDTO> list = page.getContent().stream().map(item -> {
            CookRecordDTO operationDTO = new CookRecordDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, operationDTO);
            operationDTO.setAuditorId(item.getAuditorId());
            operationDTO.setAuditorName(item.getAuditorName());

            if (null != item.getDutyEmpId()) {
                FeignEmployeeDTO employee = feignEmployeeService.findById(item.getDutyEmpId());
                operationDTO.setDutyEmpId(item.getDutyEmpId());
                operationDTO.setDutyEmpName(employee.getName());
                operationDTO.setDutyEmpAvatar(employee.getAvatar());
                operationDTO.setRole(employee.getPosition());
            }

            if (null != item.getMessProductId()) {
                FeignMessProdDto messProduct = feignMessProductService.findDtoById(item.getMessProductId());
                operationDTO.setProductId(messProduct.getId());
                operationDTO.setProductName(messProduct.getName());
                operationDTO.setProductImg(messProduct.getImg());
            }

            String tmpKey = item.getRecordTime().getLong(ChronoField.EPOCH_DAY) + "-" + item.getMessProductId();
            CommentsDTO comment = cache.get(tmpKey);
            if (comment == null) {
                comment = kitchenCookRecordRepository.findByProductIdAndTime(
                        item.getMessProductId(),
                        item.getRecordTime().with(LocalTime.MIN),
                        item.getRecordTime().with(LocalTime.MAX));
                cache.put(tmpKey, comment);
            }
            operationDTO.setCommentsInfo(CookRecordDTO.CommentsInfo.of(comment));
            return operationDTO;
        }).collect(Collectors.toList());

        return PageUtil.toPageDTO(list, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object add(CookRecordAddReq req) {
        KitchenCookRecord record = new KitchenCookRecord();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, record);

        FeignEmployeeDTO byId = this.feignEmployeeService.findById(req.getDutyEmpId());
        if (null == byId) {
            throw new BizException("后厨用户不存在");
        }

        record.setDutyEmpId(byId.getId());
        record.setDutyEmpName(byId.getName());

        FeignEmployeeDTO auditor = this.feignEmployeeService.findById(SecurityUtils.getUserId());
        if (null == auditor) {
            throw new BizException("审核人不存在");
        }
        record.setAuditorId(auditor.getId());
        record.setAuditorName(auditor.getName());

        FeignMessProdDto product = feignMessProductService.findDtoById(req.getProductId());
        if (product == null) {
            throw new BizException("", "菜品不存在");
        }
        record.setMessProductId(product.getId());
        record.setMessProductName(product.getName());

        record.setImg(req.getImg());
        kitchenCookRecordRepository.save(record);
        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_COOK, record.getState(), record.getRecordTime());
        return record;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object update(CookRecordEditReq req) {
        Optional<KitchenCookRecord> optional = kitchenCookRecordRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        KitchenCookRecord record = optional.get();
        BeanUtils.copyProperties(req, record);

        FeignEmployeeDTO byId = this.feignEmployeeService.findById(req.getDutyEmpId());
        if (null == byId) {
            throw new BizException("后厨用户不存在");
        }

        record.setDutyEmpId(byId.getId());
        record.setDutyEmpName(byId.getName());

        FeignEmployeeDTO auditor = this.feignEmployeeService.findById(SecurityUtils.getUserId());
        if (null == auditor) {
            throw new BizException("审核人不存在");
        }
        record.setAuditorId(auditor.getId());
        record.setAuditorName(auditor.getName());

        FeignMessProdDto product = feignMessProductService.findDtoById(req.getProductId());
        if (product == null) {
            throw new BizException("", "菜品不存在");
        }
        record.setMessProductId(product.getId());
        record.setMessProductName(product.getName());

        kitchenCookRecordRepository.save(record);

        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_COOK, record.getState(), record.getRecordTime());

        return record;
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        KitchenCookRecord cookRecord;
        for (Long id : ids) {
            this.kitchenCookRecordRepository.findById(id).orElseThrow(() -> new BizException("", "烹饪记录不存在"));

            kitchenCookRecordRepository.deleteById(id);
            kitchenResultService.delByRecordIdAndItemType(id, Constants.ITEM_GROUP_COOK);
        }
    }

    public BigDecimal todayPassRate() {
        List<KitchenCookRecord> inspectRecords = this.kitchenCookRecordRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }
}
