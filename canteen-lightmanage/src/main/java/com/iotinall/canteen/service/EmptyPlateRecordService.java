package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.MealTypeEnum;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateRecordDTO;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateRecordQueryCriteria;
import com.iotinall.canteen.entity.EmptyPlateImgRecord;
import com.iotinall.canteen.repository.EmptyPlateImgRecordRepository;
import com.iotinall.canteen.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 光盘行动逻辑处理类
 *
 * @author loki
 * @date 2021/7/8 16:50
 **/
@Service
public class EmptyPlateRecordService {
    @Resource
    private EmptyPlateImgRecordRepository emptyPlateImgRecordRepository;
    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    /**
     * 分页
     *
     * @author loki
     * @date 2021/05/25 10:41
     */
    public PageDTO<EmptyPlateRecordDTO> page(EmptyPlateRecordQueryCriteria criteria, Pageable pageable) {
        LocalDateTime begin = null;
        LocalDateTime end = null;
        if (StringUtils.isNotBlank(criteria.getStartTime()) && StringUtils.isNotBlank(criteria.getEndTime())) {
            begin = DateUtil.str2LocalDateTimeBegin(criteria.getStartTime());
            end = DateUtil.str2LocalDateTimeEnd(criteria.getEndTime());
        }

        Set<Long> userTenantOrgIds = SecurityUtils.getCurrentUser().getRoleTenantOrgIds();
        Set<Long> childrenIds = feignTenantOrganizationService.findAllChildren(userTenantOrgIds);

        Specification<EmptyPlateImgRecord> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("mealType", criteria.getMealType()),
                        Criterion.gte("createTime", begin),
                        Criterion.lte("createTime", end),
                        Criterion.in("tenantOrgId", childrenIds)
                ).build();
        Page<EmptyPlateImgRecord> pageResult = emptyPlateImgRecordRepository.findAll(specification, pageable);

        List<EmptyPlateImgRecord> recordList = pageResult.getContent();
        List<EmptyPlateRecordDTO> recordDTOList = new ArrayList<>(recordList.size());
        EmptyPlateRecordDTO recordDTO;
        for (EmptyPlateImgRecord record : recordList) {
            recordDTO = new EmptyPlateRecordDTO();
            recordDTOList.add(recordDTO);

            recordDTO.setId(record.getId());
            recordDTO.setRecordTime(record.getCreateTime());
            recordDTO.setViolatorsName(Collections.EMPTY_SET);
            if(null!=record.getMealType()) {
                recordDTO.setMealTypeName(MealTypeEnum.byCode(record.getMealType()).getDesc());
            }
            recordDTO.setImgUrl(record.getOriginalImgUrl());
        }

        return PageUtil.toPageDTO(recordDTOList, pageResult);
    }
}
