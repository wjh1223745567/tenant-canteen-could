package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.assessrecord.AssessMorningDto;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.item.ItemDTO;
import com.iotinall.canteen.dto.kitchen.KitchenMorningInspectRecordDTO;
import com.iotinall.canteen.dto.morninginspect.MorningInspectAddReq;
import com.iotinall.canteen.dto.morninginspect.MorningInspectDTO;
import com.iotinall.canteen.dto.morninginspect.MorningInspectEditReq;
import com.iotinall.canteen.dto.morninginspect.MorningInspectListReq;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.entity.KitchenMorningInspectRecord;
import com.iotinall.canteen.repository.KitchenMorningInspectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class KitchenMorningInspectService {
    @Resource
    private KitchenMorningInspectRepository kitchenMorningInspectRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private KitchenItemService kitchenItemService;
    @Resource
    private KitchenResultService kitchenResultService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    private static final Double MAX_NORMOR_TEMPRATURE = 37.3;

    public PageDTO<MorningInspectDTO> list(MorningInspectListReq req, Pageable pageable) {
        Specification<KitchenMorningInspectRecord> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("state", req.getState()))
                .where(Criterion.gte("recordTime", req.getBeginDate() == null ? null : req.getBeginDate().atStartOfDay()))
                .where(Criterion.lt("recordTime", req.getEndDate() == null ? null : req.getEndDate().atTime(LocalTime.MAX)))
                .where(Criterion.like("empName", req.getName()))
                .build(true);
        Page<KitchenMorningInspectRecord> page = kitchenMorningInspectRepository.findAll(specification, pageable);
        List<MorningInspectDTO> list = page.get().map(item -> {
            MorningInspectDTO recordDTO = new MorningInspectDTO();
            BeanUtils.copyProperties(item, recordDTO);
//            OrgEmployee emp = item.getEmployee();
//            recordDTO.setEmpId(emp.getId());
//            recordDTO.setAvatar(emp.getAvatar());
//            recordDTO.setName(emp.getName());
//            recordDTO.setRole(emp.getRole());
//            recordDTO.setImg(item.getImg());
//            recordDTO.setTemperature(item.getTemperature());
//
//            if (null != item.getAuditor()) {
//                recordDTO.setAuditorId(item.getAuditor().getId());
//                recordDTO.setAuditorName(item.getAuditor().getName());
//            }
            return recordDTO;
        }).collect(Collectors.toList());

        return PageUtil.toPageDTO(list, page);
    }

    public void add(MorningInspectAddReq req) {
//        KitchenMorningInspectRecord record = new KitchenMorningInspectRecord();
//        OrgEmployee ke = orgEmployeeRepository.findById(req.getEmpId()).orElse(null);
//        if (ke == null) {
//            throw new BizException("", "被检用户不存在");
//        }
//        LocalDateTime recordTime = req.getRecordTime();
//        long count = kitchenMorningInspectRepository.countCurrentDateInspect(ke.getId(), recordTime.withHour(0), recordTime.withHour(23));
//        if (count > 0) {
//            throw new BizException("", "该用户当天已晨检");
//        }
//        record.setEmpName(ke.getName());
//        record.setEmployee(ke);
//        OrgEmployee auditor = this.orgEmployeeRepository.findById(SecurityUtils.getUserId()).orElseThrow(() -> new BizException("", "审核人不存在"));
//        BeanUtils.copyProperties(req, record);
//        record.setAuditor(auditor);
//        List<ItemDTO> list = kitchenItemService.list(Constants.ITEM_GROUP_MORNING_INSPECT);
//        if (CollectionUtils.isEmpty(list)) {
//            throw new BizException("", "请先添加晨检项");
//        }
//        String jsonString = JSON.toJSONString(list);
//        record.setDetails(jsonString);
//        record = kitchenMorningInspectRepository.save(record);
//        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_MORNING_INSPECT, record.getState(), record.getRecordTime());
//
//        //健康码
//        if (StringUtils.isNotBlank(req.getHealthCode())) {
//            OrgEmployeePersonalRecords personalRecords = new OrgEmployeePersonalRecords()
//                    .setName("健康码")
//                    .setUrl(req.getHealthCode())
//                    .setEmployee(ke)
//                    .setHaveDate(req.getRecordTime().toLocalDate());
//            this.orgEmployeePersonalRecordsRepository.save(personalRecords);
//            record.setHealthCodeId(personalRecords.getId());
//        }
//        //行程码
//        if (StringUtils.isNotBlank(req.getItineraryCode())) {
//            OrgEmployeePersonalRecords personalRecords = new OrgEmployeePersonalRecords()
//                    .setName("行程码")
//                    .setUrl(req.getItineraryCode())
//                    .setEmployee(ke)
//                    .setHaveDate(req.getRecordTime().toLocalDate());
//            this.orgEmployeePersonalRecordsRepository.save(personalRecords);
//            record.setItineraryCodeId(personalRecords.getId());
//        }
//        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_MORNING_INSPECT, record.getState(), record.getRecordTime());
    }

    /**
     * 考勤添加晨检
     *
     * @author loki
     * @date 2021/02/02 16:24
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMorningInspectFromAttendanceRecord(FeignSimEmployeeDto employee, LocalDateTime recordTime, Float temperature, String img) {
        List<KitchenMorningInspectRecord> recordList = kitchenMorningInspectRepository.queryByEmpIdAndRecordTime(employee.getId(), recordTime.withHour(0), recordTime.withHour(23));
        KitchenMorningInspectRecord record;
        if (recordList.isEmpty()) {
            record = new KitchenMorningInspectRecord();
            record.setEmpId(employee.getId());
            record.setImg(img);
            record.setRecordTime(recordTime);
            record.setTemperature(temperature);
            if (temperature.doubleValue() < MAX_NORMOR_TEMPRATURE) {
                record.setState(1);
            } else {
                record.setState(0);
            }
            List<ItemDTO> list = kitchenItemService.list(Constants.ITEM_GROUP_MORNING_INSPECT);
            record.setDetails(JSON.toJSONString(list));
        } else {
            record = recordList.get(0);
            if (StringUtils.isBlank(record.getImg()) && StringUtils.isNotBlank(img)) {
                record.setImg(img);
            }
            record.setTemperature(temperature);
        }

        try {
            record = kitchenMorningInspectRepository.save(record);
            kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_MORNING_INSPECT, record.getState(), record.getRecordTime());
        } catch (Exception ex) {
            log.info("添加晨检记录失败：{}", ex.getMessage());
        }

    }

    public void edit(MorningInspectEditReq req) {
//        KitchenMorningInspectRecord record = kitchenMorningInspectRepository.findById(req.getId()).orElse(null);
//        if (record == null) { //
//            throw new BizException("", "修改的记录不存在");
//        }
//        OrgEmployee ke = orgEmployeeRepository.findById(req.getEmpId()).orElse(null);
//        if (ke == null) {
//            throw new BizException("", "被检用户不存在");
//        }
//        record.setEmpName(ke.getName());
//        record.setEmployee(ke);
//
//        LocalDateTime recordTime = req.getRecordTime();
//        long count = kitchenMorningInspectRepository.countCurrentDateInspect(ke.getId(), recordTime.withHour(0), recordTime.withHour(23));
//        if (count > 0) {
//            throw new BizException("", "该用户当天已晨检");
//        }
//
//        OrgEmployee auditor = this.orgEmployeeRepository.findById(SecurityUtils.getUserId()).orElseThrow(() -> new BizException("", "审核人不存在"));
//        BeanUtils.copyProperties(req, record);
//        record.setAuditor(auditor);
//        kitchenMorningInspectRepository.save(record);
//        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_MORNING_INSPECT, record.getState(), record.getRecordTime());
//
//        //健康码
//        if (StringUtils.isNotBlank(req.getHealthCode())) {
//            OrgEmployeePersonalRecords personalRecords = req.getHealthCodeId() != null ? this.orgEmployeePersonalRecordsRepository.findById(req.getHealthCodeId()).orElse(new OrgEmployeePersonalRecords()) : new OrgEmployeePersonalRecords();
//            personalRecords.setName("健康码")
//                    .setUrl(req.getHealthCode())
//                    .setEmployee(ke)
//                    .setHaveDate(req.getRecordTime().toLocalDate());
//            this.orgEmployeePersonalRecordsRepository.save(personalRecords);
//            record.setHealthCodeId(personalRecords.getId());
//        }
//        //行程码
//        if (StringUtils.isNotBlank(req.getItineraryCode())) {
//            OrgEmployeePersonalRecords personalRecords = req.getItineraryCodeId() != null ? this.orgEmployeePersonalRecordsRepository.findById(req.getItineraryCodeId()).orElse(new OrgEmployeePersonalRecords()) : new OrgEmployeePersonalRecords();
//            personalRecords.setName("行程码")
//                    .setUrl(req.getItineraryCode())
//                    .setEmployee(ke)
//                    .setHaveDate(req.getRecordTime().toLocalDate());
//            this.orgEmployeePersonalRecordsRepository.save(personalRecords);
//            record.setItineraryCodeId(personalRecords.getId());
//        }
//        kitchenResultService.addOrUpdate(record.getId(), Constants.ITEM_GROUP_MORNING_INSPECT, record.getState(), record.getRecordTime());
    }

    public void del(Long[] batch) {
        if (batch.length == 0) {
            throw new BizException("", "请选择需要删除项");
        }

        //kitchenMorningInspectRepository.deleteInBatch(recordList);
        kitchenResultService.batchDeleteByRecordId(batch, Constants.ITEM_GROUP_MORNING_INSPECT);
    }

    /**
     * 获取当天合格率
     *
     * @return
     */
    public BigDecimal todayPassRate() {
        List<KitchenMorningInspectRecord> inspectRecords = this.kitchenMorningInspectRepository.findAllByRecordTimeBetween(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0), LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        if (CollectionUtils.isEmpty(inspectRecords)) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(inspectRecords.stream().filter(item -> item.getState() == 1).count() * 100.0 / inspectRecords.size());
    }

    /**
     * 考核记录---晨检管理
     */
    public List<AssessMorningDto> morningList(AssessRecordContentReq condition) {
//        List<OrgEmployee> employees = this.orgEmployeeRepository.findByIdIn(condition.getEmpIds().toArray(new Long[0]));
//
//        List<AssessMorningDto> assessMorningDtos = new ArrayList<>(employees.size());
//        for (OrgEmployee employee : employees) {
//            AssessMorningDto assessMorningDto = new AssessMorningDto();
//            assessMorningDto.setEmpId(employee.getId());
//            assessMorningDto.setEmpName(employee.getName());
//            assessMorningDtos.add(assessMorningDto);
//        }
//        this.assessMorningCount(employees, condition.getBeginDate().atTime(LocalTime.MIN), condition.getEndDate().atTime(LocalTime.MAX), assessMorningDtos);
//        return assessMorningDtos;
        return null;
    }

//    private void assessMorningCount(List<OrgEmployee> employees, LocalDateTime startTime, LocalDateTime endTime, List<AssessMorningDto> assessMorningDtoList) {
//
//        String sql = " select mo.emp_id empId ," +
//                " count(if(mo.state = 1,1,null)) qualifiedTimes ," +
//                " count(if(mo.state = 0,1,null)) timesOfDisqualification" +
//                " from" +
//                " kitchen_morning_inspect_record mo " +
//                " where" +
//                " mo.emp_id in ('" + employees.stream().map(item -> item.getId().toString()).collect(Collectors.joining("','")) + "')" +
//                " and mo.record_time >= ? and mo.record_time <= ? " +
//                " and (mo.state in (1,0))" +
//                " group by mo.emp_id ";
//
//        List<AssessMorningDto> assessMorningDtos = jdbcTemplate.query(sql, new Object[]{startTime, endTime}, new BeanPropertyRowMapper<>(AssessMorningDto.class));
//
//        for (AssessMorningDto assessMorningDto : assessMorningDtoList) {
//            Optional<AssessMorningDto> morningDto = assessMorningDtos.stream().filter(item -> Objects.equal(assessMorningDto.getEmpId(), item.getEmpId())).findFirst();
//            if (morningDto.isPresent()) {
//
//                AssessMorningDto dto = morningDto.get();
//                int sundaysCount = 0;
//
//                LocalDate begin = startTime.toLocalDate();
//
//                LocalDate end = endTime.toLocalDate();
//
//                while (!begin.isAfter(end)) {
//                    HolidayDate holidayDate = this.holidayDateRepository.findByDateAndHoliday(begin, true);
//                    if (DayOfWeek.SUNDAY.getValue() != begin.getDayOfWeek().getValue()
//                            && DayOfWeek.SATURDAY.getValue() != begin.getDayOfWeek().getValue()
//                            && holidayDate == null) {
//                        sundaysCount++;
//                    }
//                    begin = begin.plusDays(1);
//                }
//
//                int frequency = sundaysCount;
//                int sum = dto.getQualifiedTimes() + dto.getTimesOfDisqualification();
//                BigDecimal passRate = BigDecimal.valueOf(dto.getQualifiedTimes() * 100.0 / sum);
//                BigDecimal unqualifiedRate = BigDecimal.valueOf(dto.getTimesOfDisqualification() * 100.0 / sum);
//                BigDecimal morningInspectionRate = BigDecimal.valueOf(dto.getQualifiedTimes() * 100.0 / frequency);
//
//                assessMorningDto.setFrequency(frequency);
//                assessMorningDto.setMorningInspectionRate(morningInspectionRate);
//
//                assessMorningDto.setQualifiedTimes(dto.getQualifiedTimes());
//                assessMorningDto.setTimesOfDisqualification(dto.getTimesOfDisqualification());
//
//                assessMorningDto.setPassRate(passRate);
//                assessMorningDto.setUnqualifiedRate(unqualifiedRate);
//            }
//        }
//    }

    /**
     * 查询员工晨检记录
     *
     * @author loki
     * @date 2021/7/13 19:43
     **/
    public List<KitchenMorningInspectRecordDTO> getEmployeeMorningInspect(Long empId, LocalDateTime date) {
        LocalDateTime begin = date.withHour(0);
        LocalDateTime end = date.withHour(23);
        List<KitchenMorningInspectRecord> recordList = kitchenMorningInspectRepository.queryByEmpIdAndRecordTime(empId, begin, end);
        if (CollectionUtils.isEmpty(recordList)) {
            return null;
        }

        List<KitchenMorningInspectRecordDTO> result = new ArrayList<>(recordList.size());
        KitchenMorningInspectRecordDTO recordDTO;
        for (KitchenMorningInspectRecord record : recordList) {
            recordDTO = new KitchenMorningInspectRecordDTO();
            result.add(recordDTO);
            BeanUtils.copyProperties(record, recordDTO);
        }

        return result;
    }
}
