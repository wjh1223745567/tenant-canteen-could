package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.constants.LightKitchenAlarmTypeEnum;
import com.iotinall.canteen.dto.lightkitchen.LightKitchenDTO;
import com.iotinall.canteen.dto.lightkitchen.LightKitchenQueryCriteria;
import com.iotinall.canteen.dto.lightkitchen.LightKitchenViolateRecordDTO;
import com.iotinall.canteen.entity.LightKitchenViolateRecord;
import com.iotinall.canteen.repository.LightKitchenViolateRecordRepository;
import com.iotinall.canteen.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 光盘行动违规记录service
 *
 * @author loki
 * @date 2021/7/6 20:44
 **/
@Service
public class LightKitchenViolateRecordService {
    @Resource
    private LightKitchenViolateRecordRepository lightKitchenViolateRecordRepository;
    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    /**
     * 分页
     *
     * @author loki
     * @date 2021/7/8 17:35
     **/
    public PageDTO<LightKitchenViolateRecordDTO> page(LightKitchenQueryCriteria criteria, Pageable pageable) {

        Set<Long> userTenantOrgIds = SecurityUtils.getCurrentUser().getRoleTenantOrgIds();
        Set<Long> childrenIds = feignTenantOrganizationService.findAllChildren(userTenantOrgIds);

        Specification<LightKitchenViolateRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("type", criteria.getType()))
                .where(
                        Criterion.in("tenantOrgId", childrenIds),
                        Criterion.gte("detectTime", criteria.getBeginTime()),
                        Criterion.lte("detectTime", criteria.getEndTime() == null ? null : criteria.getEndTime().plusDays(1))
                ).build();
        Page<LightKitchenViolateRecord> pageResult = this.lightKitchenViolateRecordRepository.findAll(spec, pageable);

        List<LightKitchenViolateRecord> recordList = pageResult.getContent();
        List<LightKitchenViolateRecordDTO> result = new ArrayList<>(recordList.size());
        LightKitchenViolateRecordDTO recordDTO;
        for (LightKitchenViolateRecord record : recordList) {
            recordDTO = new LightKitchenViolateRecordDTO();
            result.add(recordDTO);
            recordDTO.setAreaName(record.getAreaName());
            recordDTO.setType(record.getType());
            recordDTO.setDetectTime(record.getDetectTime());
            recordDTO.setImgUrl(record.getImgUrl());
        }
        return PageUtil.toPageDTO(result, pageResult);
    }

    /**
     * 批量删除
     *
     * @author loki
     * @date 2021/7/8 18:24
     **/
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("请选择需要删除的项");
        }

        for (Long id : ids) {
            this.lightKitchenViolateRecordRepository.deleteById(id);
        }
    }

    /**
     * 大屏，获取最近12张名厨亮灶图片
     *
     * @author loki
     * @date 2021/7/13 15:03
     **/
    public List<LightKitchenDTO> getLightKitchenLatest12() {
        Pageable page = PageRequest.of(0, 12, Sort.Direction.DESC, "detectTime");
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.ne("type", 5));

        Page<LightKitchenViolateRecord> pageResult = this.lightKitchenViolateRecordRepository.findAll(builder.build(), page);
        if (!CollectionUtils.isEmpty(pageResult.getContent())) {
            List<LightKitchenDTO> result = new ArrayList<>(12);
            LightKitchenDTO dto;
            for (LightKitchenViolateRecord record : pageResult.getContent()) {
                dto = new LightKitchenDTO();
                result.add(dto);
                dto.setTypeName(LightKitchenAlarmTypeEnum.getNameByCode(record.getType()));
                dto.setImg(ImgPair.getFileServer() + record.getImgUrl());
                dto.setDate(DateUtil.localDateTime2Str(record.getDetectTime()));
            }

            return result;
        }

        return null;
    }
}
