package com.iotinall.canteen.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.nacos.common.utils.ConvertUtils;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constants.ActivityConstants;
import com.iotinall.canteen.domain.ActivityAnswerRecord;
import com.iotinall.canteen.domain.ActivitySubject;
import com.iotinall.canteen.domain.ActivitySubjectOption;
import com.iotinall.canteen.domain.ActivitySurvey;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.protocol.ActivitySubjectDTO;
import com.iotinall.canteen.protocol.ActivitySurveyAddReq;
import com.iotinall.canteen.protocol.ActivitySurveyDTO;
import com.iotinall.canteen.protocol.ActivitySurveyEditReq;
import com.iotinall.canteen.protocol.ActivitySurveyQueryCriteria;
import com.iotinall.canteen.protocol.StickyAndEnableReq;
import com.iotinall.canteen.protocol.statistic.ActivityAnswerRecordDTO;
import com.iotinall.canteen.protocol.statistic.ActivitySubjectOptionStatisticDTO;
import com.iotinall.canteen.protocol.statistic.ActivitySubjectOptionStatisticExportDTO;
import com.iotinall.canteen.protocol.statistic.ActivitySubjectStatisticDTO;
import com.iotinall.canteen.protocol.statistic.ActivitySurveyStatisticDTO;
import com.iotinall.canteen.protocol.statistic.ActivityTextAnswerStatisticDTO;
import com.iotinall.canteen.protocol.statistic.ActivityTextAnswerStatisticExportDTO;
import com.iotinall.canteen.protocol.statistic.ExcelMergeUtil;
import com.iotinall.canteen.repository.ActivitySurveyRepository;
import com.iotinall.canteen.repository.AnswerRecordRepository;
import com.iotinall.canteen.repository.SubjectOptionRepository;
import com.iotinall.canteen.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 投票活动、问卷调查service
 *
 * @author joelau
 * @date 2021/05/29 16:49
 */
@Service
@Slf4j
public class ActivitySurveyService {
    @Resource
    private ActivitySurveyRepository activitySurveyRepository;
    @Resource
    private SubjectOptionRepository subjectOptionRepository;
    @Resource
    private SubjectRepository subjectRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignOrgService feignOrgService;
    @Resource
    private AnswerRecordRepository answerRecordRepository;


    /**
     * 投票活动简要分页
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public PageDTO<ActivitySurveyDTO> page(ActivitySurveyQueryCriteria criteria, Pageable pageable) {
        LocalDateTime begin = null, end = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Sort sort = Sort.by(Sort.Direction.DESC, "sticky")
                .and(Sort.by(Sort.Direction.DESC, "createTime"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        if (StringUtils.isNotBlank(criteria.getStartTime()) && StringUtils.isNotBlank(criteria.getEndTime())) {
            begin = LocalDateTime.parse(criteria.getStartTime() + " 00:00:00", df);
            end = LocalDateTime.parse(criteria.getEndTime() + " 23:59:59", df);
        }
        Specification<ActivitySurvey> specification = SpecificationBuilder.builder()
                .where(Criterion.like("title", criteria.getKeywords()),
                        Criterion.eq("type", criteria.getType()),
                        Criterion.gte("createTime", begin),
                        Criterion.lte("createTime", end)
                ).build();
        Page<ActivitySurvey> page = activitySurveyRepository.findAll(specification, pageable);
        List<ActivitySurveyDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);

    }

    /**
     * 实体转DTO
     *
     * @author joelau
     * @date 2021/05/31 16:48
     */
    private ActivitySurveyDTO transform(ActivitySurvey activitySurvey) {
        ActivitySurveyDTO activitySurveyDTO = new ActivitySurveyDTO();
        BeanUtils.copyProperties(activitySurvey, activitySurveyDTO);
        //发布范围勿删
//        activitySurveyDTO.setOrgIdList(activitySurvey.getOriginalOrgIdList());
        if (!CollectionUtils.isEmpty(activitySurvey.getSubjects())) {
            List<ActivitySubjectDTO> subjectDTOList = new ArrayList<>();
            ActivitySubjectDTO subjectDTO;
            for (ActivitySubject subject : activitySurvey.getSubjects()) {
                subjectDTO = new ActivitySubjectDTO();
                subjectDTOList.add(subjectDTO);
                BeanUtils.copyProperties(subject, subjectDTO);

                subjectDTO.setOptionList(subject.getOptions().stream().map(ActivitySubjectOption::getName).collect(Collectors.toList()));
            }
            activitySurveyDTO.setSubjects(subjectDTOList);
        }

        return activitySurveyDTO;
    }

    /**
     * 添加
     *
     * @author joelau
     * @date 2021/05/29 17:23
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(ActivitySurveyAddReq req) {
        ActivitySurvey activitySurvey = new ActivitySurvey();
        BeanUtils.copyProperties(req, activitySurvey, "subjects");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(req.getStartDate(), df);
        LocalDate end = LocalDate.parse(req.getEndDate(), df);
        activitySurvey.setStartDate(start);
        activitySurvey.setEndDate(end);
        //发布范围勿删
//        activitySurvey.setOriginalOrgIdList(req.getOrgIdList());
//        activitySurvey.setOrgIdList(orgId(req.getOrgIdList()));
        activitySurvey.setCreateTime(LocalDateTime.now());
        activitySurvey.setSubjectNumber(req.getSubjects().size());
        this.activitySurveyRepository.save(activitySurvey);

        //设置活动调查的题目
        Integer seq = 1;
        ActivitySubject subject;
        for (ActivitySurveyEditReq.SubjectReq subjectReq : req.getSubjects()) {
            if (StringUtils.isBlank(subjectReq.getName())) {
                throw new BizException("活动内容名称不能为空");
            }
            subject = new ActivitySubject();
            Integer type = subjectReq.getType();
            subject.setName(subjectReq.getName());
            subject.setType(type);
            subject.setSeq(seq);
            if (ActivityConstants.SHORT_ANSWER == type) {
                subject.setTips(subjectReq.getTips());
                subject.setSurvey(activitySurvey);
                this.subjectRepository.save(subject);
            } else {
                if (CollectionUtils.isEmpty(subjectReq.getOptionList())) {
                    throw new BizException(subject.getName() + "的选项不能为空");
                }
                //设置题目的多个选项
                subject.setSurvey(activitySurvey);
                this.subjectRepository.save(subject);
                ActivitySubjectOption subjectOption;
                //选项的排序
                Integer optionSeq = 1;
                for (String option : subjectReq.getOptionList()) {
                    if (StringUtils.isBlank(option)) {
                        throw new BizException(subject.getName() + "的选项名不能为空");
                    }
                    subjectOption = new ActivitySubjectOption();
                    subjectOption.setName(option);
                    subjectOption.setSubject(subject);
                    subjectOption.setSeq(optionSeq);
                    optionSeq++;
                    this.subjectOptionRepository.save(subjectOption);
                }
            }
            seq++;
        }
    }

    /**
     * 编辑
     *
     * @author joelau
     * @date 2021/05/29 17:23
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(ActivitySurveyEditReq req) {
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(req.getId()).orElseThrow(() -> new BizException("未找到该活动调查"));
        if (this.answerRecordRepository.existentAnswer(req.getId()) > 0) {
            throw new BizException("已有人作答，不可修改活动内容");
        }
        BeanUtils.copyProperties(req, activitySurvey, "subjects");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(req.getStartDate(), df);
        LocalDate end = LocalDate.parse(req.getEndDate(), df);
        activitySurvey.setStartDate(start);
        activitySurvey.setEndDate(end);
        //发布范围勿删
//        activitySurvey.setOriginalOrgIdList(req.getOrgIdList());
//        activitySurvey.setOrgIdList(orgId(req.getOrgIdList()));
        activitySurvey.setUpdateTime(LocalDateTime.now());
        activitySurvey.setSubjectNumber(req.getSubjects().size());
        this.activitySurveyRepository.save(activitySurvey);

        //删除题目的选项
        Set<ActivitySubject> subjects = activitySurvey.getSubjects();
        for (ActivitySubject subject : subjects) {
            this.subjectOptionRepository.deleteAllBySubject(subject);
        }
        //删除活动调查的所有原题目
        this.subjectRepository.deleteAllBySurvey(activitySurvey);
        //设置活动调查的题目
        Integer seq = 1;
        ActivitySubject subject;
        for (ActivitySurveyEditReq.SubjectReq subjectReq : req.getSubjects()) {
            subject = new ActivitySubject();
            Integer type = subjectReq.getType();
            subject.setName(subjectReq.getName());
            subject.setType(type);
            subject.setSeq(seq);
            if (ActivityConstants.SHORT_ANSWER == type) {
                subject.setTips(subjectReq.getTips());
                subject.setSurvey(activitySurvey);
                this.subjectRepository.save(subject);
            } else {
                //设置题目的多个选项
                subject.setSurvey(activitySurvey);
                this.subjectRepository.save(subject);
                ActivitySubjectOption subjectOption;
                //选项的排序
                Integer optionSeq = 1;
                for (String option : subjectReq.getOptionList()) {
                    subjectOption = new ActivitySubjectOption();
                    subjectOption.setName(option);
                    subjectOption.setSubject(subject);
                    subjectOption.setSeq(optionSeq);
                    optionSeq++;
                    this.subjectOptionRepository.save(subjectOption);
                }
            }
            seq++;
        }
    }

    /**
     * 删除
     *
     * @author joelau
     * @date 2021/05/29 17:23
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        String[] idList = ids.split(",");
        Long item;
        for (String id : idList) {
            item = Long.parseLong(id);
            ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(item).orElseThrow(() -> new BizException("未找到该活动调查"));
            if (this.answerRecordRepository.existentAnswer(item) > 0) {
                throw new BizException("已有人作答，不可删除活动内容");
            }
            Set<ActivitySubject> subjects = activitySurvey.getSubjects();
            for (ActivitySubject subject : subjects) {
                this.subjectOptionRepository.deleteAllBySubject(subject);
            }
            this.subjectRepository.deleteAllBySurvey(activitySurvey);
            this.activitySurveyRepository.delete(activitySurvey);
        }
    }

    /**
     * 投票活动统计
     *
     * @author joelau
     * @date 2021/05/29 17:23
     */
    public ActivitySurveyStatisticDTO statisticList(Long id) {
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(id).orElseThrow(() -> new BizException("未找到该活动调查"));
        ActivitySurveyStatisticDTO activitySurveyStatisticDTO = new ActivitySurveyStatisticDTO();
        Long surveyId = activitySurvey.getId();
        Integer totalNumber = this.feignEmployeeService.findSimAll().size();
        //发布范围勿删
//                this.feignEmployeeService.countEmployee(activitySurvey.getOrgIdList());
        Integer submittedNumber = this.answerRecordRepository.countSubmittedEmployee(surveyId);
        activitySurveyStatisticDTO.setStartDate(activitySurvey.getStartDate());
        activitySurveyStatisticDTO.setEndDate(activitySurvey.getEndDate());
        activitySurveyStatisticDTO.setId(activitySurvey.getId());
        activitySurveyStatisticDTO.setTitle(activitySurvey.getTitle());
        activitySurveyStatisticDTO.setTotalNumber(totalNumber);
        activitySurveyStatisticDTO.setSubmittedNumber(submittedNumber);
        return activitySurveyStatisticDTO;
    }

    /**
     * 投票活动统计题目详情
     *
     * @author joelau
     * @date 2021/05/29 17:23
     */
    public List<ActivitySubjectStatisticDTO> statisticSubjectList(Long id) {
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(id).orElseThrow(() -> new BizException("未找到该活动调查"));
        List<ActivityAnswerRecordDTO> recordDTOS = this.answerRecordRepository.activityOptions(id);
        Set<ActivitySubject> subjects = activitySurvey.getSubjects();
        if (CollectionUtils.isEmpty(subjects)) {
            return null;
        }

        List<ActivitySubjectStatisticDTO> subjectResults = new ArrayList<>();
        ActivitySubjectStatisticDTO subjectResult;
        for (ActivitySubject subject : subjects) {
            if (!subject.getType().equals(ActivityConstants.SHORT_ANSWER)) {
                subjectResult = new ActivitySubjectStatisticDTO();
                subjectResult.setId(subject.getId());
                subjectResult.setSeq(subject.getSeq());
                subjectResult.setName(subject.getName());
                subjectResult.setType(subject.getType());
                List<ActivitySubjectOptionStatisticDTO> subjectOptionResults = new ArrayList<>();
                ActivitySubjectOptionStatisticDTO subjectOptionResult;
                List<ActivitySubjectOption> options = subject.getOptions();
                if (!CollectionUtils.isEmpty(options)) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    for (ActivitySubjectOption option : options) {
                        subjectOptionResult = new ActivitySubjectOptionStatisticDTO();
                        subjectOptionResult.setId(option.getId());
                        subjectOptionResult.setName(option.getName());
                        subjectOptionResult.setSelectedNum(0);
                        subjectOptionResult.setPercent(df.format((float) 0) + "%");
                        for (ActivityAnswerRecordDTO recordDTO : recordDTOS) {
                            if (option.getName().equals(recordDTO.getName())
                                    && option.getSubject().getId().equals(recordDTO.getSubjectId())
                                    && option.getId().equals(recordDTO.getOptionId())) {
                                subjectOptionResult.setSelectedNum(recordDTO.getCount());
                                Integer submittedNumber = this.answerRecordRepository.countSubmittedEmployee(id);
                                subjectOptionResult.setPercent(df.format((float) recordDTO.getCount() * 100 / submittedNumber) + "%");
                            }
                        }
                        subjectOptionResults.add(subjectOptionResult);
                    }
                }

                subjectResult.setOptions(subjectOptionResults);
                subjectResults.add(subjectResult);
            }
        }
        return subjectResults;
    }

    /**
     * 投票活动统计问答题答案详情
     *
     * @author joelau
     * @date 2021/06/04 11:37
     */
    public List<ActivitySubjectStatisticDTO> statisticTextAnswer(Long id) {
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(id).orElseThrow(() -> new BizException("未找到该活动调查"));
        Set<ActivitySubject> subjects = activitySurvey.getSubjects();
        if (CollectionUtils.isEmpty(subjects)) {
            return null;
        }
        List<ActivitySubjectStatisticDTO> subjectResults = new ArrayList<>();
        ActivitySubjectStatisticDTO subjectResult;
        for (ActivitySubject subject : subjects) {
            if (subject.getType().equals(ActivityConstants.SHORT_ANSWER)) {
                subjectResult = new ActivitySubjectStatisticDTO();
                subjectResult.setId(subject.getId());
                subjectResult.setSeq(subject.getSeq());
                subjectResult.setName(subject.getName());
                subjectResult.setType(subject.getType());
                List<ActivityTextAnswerStatisticDTO> textAnswerResults = new ArrayList<>();
                ActivityTextAnswerStatisticDTO textAnswerResult;
                List<ActivityAnswerRecord> records = this.answerRecordRepository.findAllByActivitySurvey_IdAndSubject_IdAndFinished(id, subject.getId(), true);
                for (ActivityAnswerRecord record : records) {
                    FeignEmployeeDTO feignEmployeeDTO = this.feignEmployeeService.findById(record.getEmpId());
                    textAnswerResult = new ActivityTextAnswerStatisticDTO();
                    textAnswerResult.setId(record.getId());
                    textAnswerResult.setTextAnswer(record.getTextAnswer());
                    if (null != feignEmployeeDTO) {
                        textAnswerResult.setEmpName(feignEmployeeDTO.getName());
                    }
                    textAnswerResults.add(textAnswerResult);
                }
                subjectResult.setTextAnswers(textAnswerResults);
                subjectResults.add(subjectResult);
            }
        }
        return subjectResults;
    }


    /**
     * 根据一些部门ID查其所有子部门的ID
     *
     * @return
     * @author joelau
     * @date 2021/07/15 16:47
     */
    private String orgId(String previous) {
        List<Long> previousIds = Arrays.stream(previous.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        Set<Long> afterIds = new HashSet<>(previousIds);
        if (!CollectionUtils.isEmpty(previousIds)) {
            Set<Long> childIds = this.feignOrgService.getAllChildOrg(previousIds);
            if (!CollectionUtils.isEmpty(childIds)) {
                afterIds.addAll(childIds);
            }
        }
        return StringUtils.join(afterIds, ",");
    }

    /**
     * 是否启用
     *
     * @author loki
     * @date 2021/06/02 17:54
     */
    public void enable(StickyAndEnableReq req) {
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(req.getId()).orElseThrow(() -> new BizException("记录不存在"));
        activitySurvey.setState(req.getState());
        this.activitySurveyRepository.save(activitySurvey);
    }

    /**
     * 是否置顶
     *
     * @author loki
     * @date 2021/06/02 17:54
     */
    public void sticky(StickyAndEnableReq req) {
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(req.getId()).orElseThrow(() -> new BizException("记录不存在"));
        activitySurvey.setSticky(req.getSticky());
        this.activitySurveyRepository.save(activitySurvey);
    }

    /**
     * 导出统计数据
     *
     * @author joelau
     * @date 2021/06/04 14:03
     */
    public void statisticExport(HttpServletResponse response, Long id) throws IOException {
        //选择题统计
        List<ActivitySubjectOptionStatisticExportDTO> optionsList = new ArrayList<>();
        ActivitySubjectOptionStatisticExportDTO activitySubjectOptionStatisticExportDTO;
        List<ActivitySubjectStatisticDTO> optionSubjectList = this.statisticSubjectList(id);
        for (ActivitySubjectStatisticDTO subject : optionSubjectList) {
            List<ActivitySubjectOptionStatisticDTO> optionList = subject.getOptions();
            for (ActivitySubjectOptionStatisticDTO option : optionList) {
                activitySubjectOptionStatisticExportDTO = new ActivitySubjectOptionStatisticExportDTO();
                activitySubjectOptionStatisticExportDTO.setSubjectName(subject.getName());
                activitySubjectOptionStatisticExportDTO.setSubjectType(subject.getType() == 0 ? "单选题" : "多选题");
                activitySubjectOptionStatisticExportDTO.setSubjectOptionName(option.getName());
                activitySubjectOptionStatisticExportDTO.setSelectedNum(option.getSelectedNum());
                activitySubjectOptionStatisticExportDTO.setPercent(option.getPercent());
                optionsList.add(activitySubjectOptionStatisticExportDTO);
            }
        }

        //问答题统计
        List<ActivityTextAnswerStatisticExportDTO> textAnswersList = new ArrayList<>();
        ActivityTextAnswerStatisticExportDTO activityTextAnswerStatisticExportDTO;
        List<ActivitySubjectStatisticDTO> textAnswerSubjectList = this.statisticTextAnswer(id);
        for (ActivitySubjectStatisticDTO subject : textAnswerSubjectList) {
            List<ActivityTextAnswerStatisticDTO> textAnswerList = subject.getTextAnswers();
            for (ActivityTextAnswerStatisticDTO textAnswer : textAnswerList) {
                activityTextAnswerStatisticExportDTO = new ActivityTextAnswerStatisticExportDTO();
                activityTextAnswerStatisticExportDTO.setSubjectName(subject.getName());
                activityTextAnswerStatisticExportDTO.setSubjectType("问答题");
                activityTextAnswerStatisticExportDTO.setTextAnswer(textAnswer.getTextAnswer());
                activityTextAnswerStatisticExportDTO.setEmpName(textAnswer.getEmpName());
                textAnswersList.add(activityTextAnswerStatisticExportDTO);
            }
        }
        int[] mergeColumnIndex = {1, 2, 0};
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).registerWriteHandler(new ExcelMergeUtil(1, mergeColumnIndex)).build();

        WriteSheet sheet01 = EasyExcel.writerSheet(0, "选择题选项统计表").head(ActivitySubjectOptionStatisticExportDTO.class).build();
        WriteSheet sheet02 = EasyExcel.writerSheet(1, "问答题答案统计表").head(ActivityTextAnswerStatisticExportDTO.class).build();
        excelWriter.write(optionsList, sheet01);
        excelWriter.write(textAnswersList, sheet02);
        excelWriter.finish();
    }
}
