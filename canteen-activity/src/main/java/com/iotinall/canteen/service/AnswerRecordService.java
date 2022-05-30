package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.ActivityConstants;
import com.iotinall.canteen.domain.ActivityAnswerRecord;
import com.iotinall.canteen.domain.ActivitySubject;
import com.iotinall.canteen.domain.ActivitySubjectOption;
import com.iotinall.canteen.domain.ActivitySurvey;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.protocol.ActivitySubjectOptionDTO;
import com.iotinall.canteen.protocol.app.ActivitySurveyAppDTO;
import com.iotinall.canteen.protocol.app.AnswerRecordAddReq;
import com.iotinall.canteen.protocol.app.SubjectAppDTO;
import com.iotinall.canteen.repository.ActivitySurveyRepository;
import com.iotinall.canteen.repository.AnswerRecordRepository;
import com.iotinall.canteen.repository.SubjectOptionRepository;
import com.iotinall.canteen.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * APP投票活动、问卷调查service
 *
 * @author joelau
 * @date 2021/06/01 10:52
 */
@Service
@Slf4j
public class AnswerRecordService {
    @Resource
    private ActivitySurveyRepository activitySurveyRepository;
    @Resource
    private AnswerRecordRepository answerRecordRepository;
    @Resource
    private SubjectOptionRepository subjectOptionRepository;
    @Resource
    private SubjectRepository subjectRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 得单个题目详情
     *
     * @param surveyId
     * @param seq
     * @return
     */
    public SubjectAppDTO getNextSubject(Long surveyId, Integer seq) {
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(surveyId).orElseThrow(() -> new BizException("未找到该活动调查"));
        ActivitySubject subject = this.subjectRepository.findBySeqAndSurvey(seq, activitySurvey);
        if (ObjectUtils.isEmpty(subject)) {
            throw new BizException("获取活动内容失败");
        }

        SubjectAppDTO subjectAppDTO = new SubjectAppDTO();
        subjectAppDTO.setId(subject.getId());
        subjectAppDTO.setTitle(subject.getName());
        subjectAppDTO.setType(subject.getType());
        subjectAppDTO.setTips(subject.getTips());
        subjectAppDTO.setSeq(subject.getSeq());
        subjectAppDTO.setLast(seq.intValue() == activitySurvey.getSubjectNumber());

        //可选项
        if (!CollectionUtils.isEmpty(subject.getOptions())) {
            List<ActivitySubjectOptionDTO> list = new ArrayList<>();
            List<ActivitySubjectOption> options = subject.getOptions();
            ActivitySubjectOptionDTO subjectOptionDTO;
            for (ActivitySubjectOption subjectOption : options) {
                subjectOptionDTO = new ActivitySubjectOptionDTO();
                subjectOptionDTO.setId(subjectOption.getId());
                subjectOptionDTO.setName(subjectOption.getName());
                list.add(subjectOptionDTO);
            }
            subjectAppDTO.setOptionList(list);
        }
        //已经做过存在答案的题目
        List<ActivityAnswerRecord> records = this.answerRecordRepository.findAllByActivitySurvey_IdAndSubject_IdAndEmpId(activitySurvey.getId(), subject.getId(), SecurityUtils.getUserId());
        if (!CollectionUtils.isEmpty(records)) {
            List<ActivitySubjectOptionDTO> results = new ArrayList<>();
            ActivitySubjectOptionDTO activitySubjectOptionDTO;
            for (ActivityAnswerRecord record : records) {
                if (!ObjectUtils.isEmpty(record.getOption())) {
                    activitySubjectOptionDTO = new ActivitySubjectOptionDTO();
                    activitySubjectOptionDTO.setId(record.getOption().getId());
                    activitySubjectOptionDTO.setName(record.getOption().getName());
                    results.add(activitySubjectOptionDTO);
                } else if (!StringUtils.isEmpty(record.getTextAnswer()) && ObjectUtils.isEmpty(record.getOption())) {
                    subjectAppDTO.setTextAnswer(record.getTextAnswer());
                }
            }
            subjectAppDTO.setResults(results);
        }

        return subjectAppDTO;
    }

    /**
     * 新建一个题目的答题记录
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void createAnswerRecord(AnswerRecordAddReq req) {
        ActivitySubject subject = this.subjectRepository.findById(req.getSubjectId()).orElseThrow(() -> new BizException("内容不存在"));
        FeignEmployeeDTO orgEmployee = this.feignEmployeeService.findById(SecurityUtils.getUserId());
        if (null == orgEmployee) {
            throw new BizException("人员不存在");
        }
        ActivitySurvey activitySurvey = this.activitySurveyRepository.findById(req.getSurveyId()).orElseThrow(() -> new BizException("活动调查不存在"));
        //删除这道题原有的答案选项
        this.answerRecordRepository.deleteAllBySubject_IdAndEmpId(req.getSubjectId(), SecurityUtils.getUserId());
        //判断题目类型
        if (ActivityConstants.SHORT_ANSWER == req.getType()) {
            //创建新的答案记录
            ActivityAnswerRecord answerRecord = new ActivityAnswerRecord();
            answerRecord.setEmpId(orgEmployee.getId());
            answerRecord.setActivitySurvey(activitySurvey);
            answerRecord.setSubject(subject);
            if (!StringUtils.isBlank(req.getTextAnswer())) {
                answerRecord.setTextAnswer(req.getTextAnswer());
            }
            this.answerRecordRepository.save(answerRecord);
        } else {
            List<Long> optionIds = req.getOptionIds();
            if (!CollectionUtils.isEmpty(optionIds)) {
                ActivityAnswerRecord answerRecord;
                for (Long oid : optionIds) {
                    //创建新的答案记录
                    ActivitySubjectOption option = this.subjectOptionRepository.findById(oid).orElseThrow(() -> new BizException("选项内容不存在"));
                    answerRecord = new ActivityAnswerRecord();
                    answerRecord.setEmpId(orgEmployee.getId());
                    answerRecord.setActivitySurvey(activitySurvey);
                    answerRecord.setSubject(subject);
                    answerRecord.setOption(option);
                    this.answerRecordRepository.save(answerRecord);
                }
            } else {
                ActivityAnswerRecord answerRecord = new ActivityAnswerRecord();
                answerRecord.setEmpId(orgEmployee.getId());
                answerRecord.setActivitySurvey(activitySurvey);
                answerRecord.setSubject(subject);
                this.answerRecordRepository.save(answerRecord);
            }
        }
    }

    /**
     * 将一个调查的答题记录设置为完成
     */
    @Transactional(rollbackFor = Exception.class)
    public void finishAnswerRecord(Long surveyId, Long empId) {
        List<ActivityAnswerRecord> list = this.answerRecordRepository.findAllByActivitySurvey_IdAndEmpId(surveyId, empId);
        for (ActivityAnswerRecord answerRecord : list) {
            if (null == answerRecord.getOption() && null == answerRecord.getTextAnswer()) {
                throw new BizException("有题目未作答，请答题后再点击完成");
            }
            answerRecord.setFinished(true);
            this.answerRecordRepository.save(answerRecord);
        }
    }

    /**
     * 个人所能查询到的活动调查分页
     *
     * @param empId
     * @return
     */
    public PageDTO<ActivitySurveyAppDTO> list(Long empId, Integer type, Pageable pageable) {
        FeignEmployeeDTO employee = this.feignEmployeeService.findById(empId);
        if (null == employee) {
            throw new BizException("人员不存在");
        }
        //勿删发布范围
//        if (null == employee.getOrgId()) {
//            return null;
//        }
        Sort sort = Sort.by(Sort.Direction.DESC, "sticky")
                .and(Sort.by(Sort.Direction.DESC, "create_time"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<ActivitySurvey> page = activitySurveyRepository.queryByType(type, pageable);
//        Page<ActivitySurvey> page = activitySurveyRepository.queryByOrg(employee.getOrgId(), type, pageable);
        List<ActivitySurveyAppDTO> list = page.getContent().stream().map((activitySurvey) -> {
            ActivitySurveyAppDTO appDTO = new ActivitySurveyAppDTO();
            appDTO.setId(activitySurvey.getId());
            appDTO.setTitle(activitySurvey.getTitle());
            String s = activitySurvey.getStartDate() + "至" + activitySurvey.getEndDate();
            appDTO.setEffectiveDate(s);
            appDTO.setImgUrl(activitySurvey.getImgUrl());
            appDTO.setSubjectNumber(activitySurvey.getSubjectNumber());
            appDTO.setDescription(activitySurvey.getDescription());
            if (activitySurvey.getEndDate().compareTo(LocalDate.now()) > -1 && activitySurvey.getStartDate().compareTo(LocalDate.now()) < 1) {
                if (this.answerRecordRepository.existentFinished(activitySurvey.getId(), empId) > 0) {
                    appDTO.setSurveyState(ActivityConstants.PARTICIPATED);
                } else {
                    appDTO.setSurveyState(ActivityConstants.PROCESSING);
                }
            } else if (activitySurvey.getStartDate().isAfter(LocalDate.now())) {
                appDTO.setSurveyState(ActivityConstants.NOT_ARRIVED);
            } else {
                appDTO.setSurveyState(ActivityConstants.EXPIRED);
            }
            return appDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }
}
