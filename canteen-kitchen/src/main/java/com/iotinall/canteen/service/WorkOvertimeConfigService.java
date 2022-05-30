package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.overtime.WorkOvertimeConfigAddReq;
import com.iotinall.canteen.dto.overtime.WorkOvertimeConfigCondition;
import com.iotinall.canteen.dto.overtime.WorkOvertimeConfigDto;
import com.iotinall.canteen.entity.WorkOvertimeConfig;
import com.iotinall.canteen.repository.WorkOvertimeConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 加班规则
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkOvertimeConfigService {

    @Resource
    private WorkOvertimeConfigRepository configRepository;

    public PageDTO<WorkOvertimeConfigDto> findPage(WorkOvertimeConfigCondition condition, Pageable pageable) {
        Specification<WorkOvertimeConfig> specification = SpecificationBuilder.builder()
                .where(Criterion.like("name", condition.getName()))
                .build();
        Page<WorkOvertimeConfigDto> page = this.configRepository.findAll(specification, pageable).map(item -> {
            WorkOvertimeConfigDto configDto = new WorkOvertimeConfigDto()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setOnHolidaysEnable(item.getOnHolidaysEnable())
                    .setOnHolidaysLess(item.getOnHolidaysLess())
                    .setOnHolidaysStart(item.getOnHolidaysStart())
                    .setOnHolidaysType(item.getOnHolidaysType())
                    .setOnRestDaysEnable(item.getOnRestDaysEnable())
                    .setOnRestDaysLess(item.getOnRestDaysLess())
                    .setOnRestDaysStart(item.getOnRestDaysStart())
                    .setOnRestDaysType(item.getOnRestDaysType())
                    .setWorkOverLess(item.getWorkOverLess())
                    .setWorkOverStart(item.getWorkOverStart())
                    .setWorkOvertimeEnable(item.getWorkOvertimeEnable())
                    .setWorkOverType(item.getWorkOverType());
            return configDto;
        });
        return PageUtil.toPageDTO(page);
    }

    /**
     * 添加配置
     *
     * @param req
     */
    public void add(WorkOvertimeConfigAddReq req) {
        WorkOvertimeConfig overtimeConfig = new WorkOvertimeConfig()
                .setWorkOvertimeEnable(req.getWorkOvertimeEnable())
                .setName(req.getName())
                .setOnHolidaysEnable(req.getOnHolidaysEnable())
                .setOnHolidaysLess(req.getOnHolidaysLess())
                .setOnHolidaysStart(req.getOnHolidaysStart())
                .setOnHolidaysType(req.getOnHolidaysType())
                .setOnRestDaysEnable(req.getOnRestDaysEnable())
                .setOnRestDaysLess(req.getOnRestDaysLess())
                .setOnRestDaysStart(req.getOnRestDaysStart())
                .setOnRestDaysType(req.getOnRestDaysType())
                .setWorkOverLess(req.getWorkOverLess())
                .setWorkOverStart(req.getWorkOverStart())
                .setWorkOverType(req.getWorkOverType());
        this.configRepository.save(overtimeConfig);
    }

    /**
     * 编辑
     */
    public void edit(WorkOvertimeConfigAddReq req) {
        if (req.getId() == null) {
            throw new BizException("", "ID不能为空");
        }
        WorkOvertimeConfig overtimeConfig = this.configRepository.findById(req.getId()).orElseThrow(() -> new BizException("", "未找到配置数据"));
        overtimeConfig.setWorkOvertimeEnable(req.getWorkOvertimeEnable())
                .setName(req.getName())
                .setOnHolidaysEnable(req.getOnHolidaysEnable())
                .setOnHolidaysLess(req.getOnHolidaysLess())
                .setOnHolidaysStart(req.getOnHolidaysStart())
                .setOnHolidaysType(req.getOnHolidaysType())
                .setOnRestDaysEnable(req.getOnRestDaysEnable())
                .setOnRestDaysLess(req.getOnRestDaysLess())
                .setOnRestDaysStart(req.getOnRestDaysStart())
                .setOnRestDaysType(req.getOnRestDaysType())
                .setWorkOverLess(req.getWorkOverLess())
                .setWorkOverStart(req.getWorkOverStart())
                .setWorkOverType(req.getWorkOverType());
        this.configRepository.save(overtimeConfig);
    }

    /**
     * 删除
     */
    public void batchDelete(Long[] ids) {
        for (Long id : ids) {
            this.configRepository.deleteById(id);
        }
    }
}
