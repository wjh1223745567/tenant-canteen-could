package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.AssessRecordContentEnum;
import com.iotinall.canteen.constant.AssessRecordTypeEnum;
import com.iotinall.canteen.dto.assessrecord.*;
import com.iotinall.canteen.entity.KitchenAssessRecord;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.dto.organization.FeignCookDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.KitchenAssessRecordRepository;
import com.iotinall.canteen.repository.KitchenItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考核记录service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KitchenAssessRecordService {
    @Resource
    private KitchenAssessRecordRepository kitchenAssessRecordRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenItemRepository kitchenItemRepository;
    @Resource
    private AttendanceRecordService attendanceRecordService;
    @Resource
    private KitchenGarbageRecordService garbageRecordService;
    @Resource
    private KitchenFacilityRecordService facilityRecordService;
    @Resource
    private KitchenEnvInspectRecordService envInspectRecordService;
    @Resource
    private KitchenDisinfectService disinfectService;
    @Resource
    private KitchenMorningInspectService kitchenMorningInspectService;
    @Resource
    private KitchenSampleService sampleService;

    public PageDTO<AssessRecordDTO> list(AssessRecordListReq req, Pageable pageable) {
        Specification<KitchenAssessRecord> specification = SpecificationBuilder.builder()
                .where(Criterion.gte("beginDate", req.getBeginDate()))
                .where(Criterion.lte("endDate", req.getEndDate()))
                .where(Criterion.eq("typ", req.getTyp()))
                .where(Criterion.like("empName", req.getName()))
                .build();
        Page<KitchenAssessRecord> page = kitchenAssessRecordRepository.findAll(specification, pageable);
        List<AssessRecordDTO> collect = page.get().map(item -> {
            AssessRecordDTO recordDTO = new AssessRecordDTO();
            BeanUtils.copyProperties(item, recordDTO);
            recordDTO.setItemId(item.getItem().getId());
            recordDTO.setItemName(item.getItem().getName());
            recordDTO.setCheckingTime(this.changeDate(item.getBeginDate(), item.getTyp()));

            if (null != item.getEmpId()) {
                recordDTO.setUsed(Objects.equals(SecurityUtils.getUserId(), item.getEmpId()));
                FeignEmployeeDTO emp = feignEmployeeService.findById(item.getEmpId());
                recordDTO.setEmpAvatar(emp.getAvatar());
                recordDTO.setEmpId(emp.getId());
                recordDTO.setEmpName(emp.getName());
                recordDTO.setRole(emp.getPosition());
            }
            return recordDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(collect, page);
    }

    /**
     * 转换为日期
     *
     * @return
     */
    private String changeDate(LocalDate beginDate, Integer typ) {
        switch (AssessRecordTypeEnum.findByCode(typ)) {
            case MONTHLY: {
                if (beginDate != null) {
                    return beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                }
                break;
            }
            case QUARTERLY: {
                if (beginDate != null) {
                    String year = beginDate.format(DateTimeFormatter.ofPattern("yyyy"));
                    Integer monthValue = beginDate.getMonthValue();
                    if (monthValue < 4) {
                        return year + "年第一季";
                    } else if (monthValue < 7) {
                        return year + "年第二季";
                    } else if (monthValue < 10) {
                        return year + "年第三季";
                    } else {
                        return year + "年第四季";
                    }
                }
                break;
            }
            case YEAR: {
                if (beginDate != null) {
                    return beginDate.format(DateTimeFormatter.ofPattern("yyyy"));
                }
                break;
            }
        }
        return null;
    }

    /**
     * 查询所有合格不合格
     *
     * @param empReq
     * @return
     */
    public List<FeignCookDto> listEmp(AssessRecordEmpReq empReq) {
        if (empReq.getAssessed() == null) {
            //全部人
            return this.feignEmployeeService.findAllCook();
        } else if (empReq.getAssessed()) {
            //已审核人
            Set<Long> ids = this.findAssessedEmpId(empReq);
            List<FeignCookDto> cookDtoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(ids)) {
                FeignSimEmployeeDto employee;
                FeignCookDto cookDto;
                for (Long id : ids) {
                    employee = this.feignEmployeeService.findSimById(id);
                    if (null != employee) {
                        cookDto = new FeignCookDto();
                        cookDtoList.add(cookDto);
                        cookDto.setId(employee.getId());
                        cookDto.setName(employee.getName());
                    }
                }
            }

            return cookDtoList;
        } else if (!empReq.getAssessed()) {
            //未审核人
            List<FeignCookDto> all = this.feignEmployeeService.findAllCook();
            Set<Long> ids = this.findAssessedEmpId(empReq);
            return all.stream().filter(item -> !ids.contains(item.getId())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 考核内容
     *
     * @param contentReq
     * @return
     */
    public List<?> listContent(AssessRecordContentReq contentReq) {
        if (Objects.equals(AssessRecordTypeEnum.MONTHLY.getCode(), contentReq.getTyp())) {
            //月度
            if (contentReq.getContentType() == null) {
                throw new BizException("", "考核内容类型不能为空");
            }
            switch (AssessRecordContentEnum.findByCode(contentReq.getContentType())) {
                case ATTENDANCE: {
                    //考勤记录统计
                    return this.attendanceRecordService.recordReckonList(contentReq);
                }
                case EQUIPMENT: {
                    return this.facilityRecordService.facilityList(contentReq);
                }
                case FOOD_WASTE: {
                    //厨余垃圾
                    return this.garbageRecordService.garbageList(contentReq);
                }
                case ENVIRONMENT: {
                    return this.envInspectRecordService.envspectList(contentReq);
                }
                case DISINFECTION: {
                    return this.disinfectService.disinfectList(contentReq);
                }
                case MORNING_CHECK: {
                    return this.kitchenMorningInspectService.morningList(contentReq);
                }
                case RESERVE_SAMPLE: {
                    return this.sampleService.sampleList(contentReq);
                }
                default: {
                    throw new BizException("", "未知类型");
                }
            }

        } else {
            //季度  年度
            Specification<KitchenAssessRecord> specification = SpecificationBuilder.builder()
                    //年度查季度，季度查月度
                    .where(Criterion.eq("typ", contentReq.getTyp() - 1),
                            Criterion.gte("beginDate", contentReq.getBeginDate()),
                            Criterion.lte("endDate", contentReq.getEndDate()),
                            Criterion.in("empId", contentReq.getEmpIds())
                    )
                    .build();

            List<AssessRecordTableListDto> tableListDtos = this.kitchenAssessRecordRepository.findAll(specification)
                    .stream()
                    .map(item -> {
                        AssessRecordTableListDto listDto = new AssessRecordTableListDto()
                                .setTime(this.changeDate(item.getBeginDate(), item.getTyp()))
                                .setComment(item.getComments())
                                .setResult(item.getItem() != null ? item.getItem().getName() : null);
                        if (item.getEmpId() != null) {
                            FeignEmployeeDTO employee = feignEmployeeService.findById(item.getEmpId());
                            if (null != employee) {
                                listDto.setEmpName(employee.getName());
                                listDto.setEmpRole(employee.getPosition());
                            }
                        }
                        return listDto;
                    }).collect(Collectors.toList());
            return tableListDtos;
        }
    }

    private Set<Long> findAssessedEmpId(AssessRecordEmpReq empReq) {
        Specification<KitchenAssessRecord> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("beginDate", empReq.getBeginDate()),
                        Criterion.eq("endDate", empReq.getEndDate()),
                        Criterion.eq("typ", empReq.getTyp()))
                .build();

        return this.kitchenAssessRecordRepository.findAll(specification)
                .stream()
                .map(KitchenAssessRecord::getEmpId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void add(AssessRecordAddReq req) {
        FeignEmployeeDTO employee;
        KitchenItem kitchenItem;
        for (Long empId : req.getEmpIds()) {
            employee = feignEmployeeService.findById(empId);
            if (null == employee) {
                throw new BizException("考核人员不存在");
            }

            kitchenItem = kitchenItemRepository.findById(req.getItemId()).orElseThrow(() -> new BizException("", "考核标准不存在"));

            KitchenAssessRecord record = this.kitchenAssessRecordRepository.findByUnique(empId, req.getTyp(), req.getBeginDate(), req.getEndDate());
            if (record == null) {
                record = new KitchenAssessRecord();
            }

            record.setAuditorId(SecurityUtils.getUserId())
                    .setBeginDate(req.getBeginDate())
                    .setComments(req.getComments())
                    .setEndDate(req.getEndDate())
                    .setEmpId(empId)
                    .setEmpName(employee.getName())
                    .setItem(kitchenItem)
                    .setTyp(req.getTyp());
            kitchenAssessRecordRepository.save(record);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Object del(Long[] batch) {
        if (batch.length <= 0) {
            throw new BizException("", "请选择需要删除的考核记录");
        }

        List<KitchenAssessRecord> assessRecordList = new ArrayList<>();
        KitchenAssessRecord assessRecord;
        for (Long id : batch) {
            assessRecord = this.kitchenAssessRecordRepository.findById(id).orElseThrow(() -> new BizException("", "考核记录不存在"));
            assessRecordList.add(assessRecord);
        }

        this.kitchenAssessRecordRepository.deleteAll(assessRecordList);
        return assessRecordList;
    }

}
