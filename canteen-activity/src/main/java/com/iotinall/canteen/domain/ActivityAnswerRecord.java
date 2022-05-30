package com.iotinall.canteen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 单个题目答题记录
 *
 * @author joelau
 * @date 2021/05/29 14:00
 */
@Data
@Entity
@Table(name = "activity_answer_record")
@ToString(exclude = {"subject", "activitySurvey"})
@EqualsAndHashCode(callSuper = true, exclude = {"subject", "activitySurvey"})
@JsonIgnoreProperties(value = {"subject", "activitySurvey"})
public class ActivityAnswerRecord extends BaseEntity {

    /**
     * 答题者
     */
    private Long empId;

    /**
     * 题目
     */
    @OneToOne
    @JoinColumn(name = "subject_id")
    private ActivitySubject subject;

    /**
     * 活动调查(投票活动/问卷调查)
     */
    @OneToOne
    @JoinColumn(name = "activity_survey_id")
    private ActivitySurvey activitySurvey;

    /**
     * 题目答案选项
     */
    @OneToOne
    @JoinColumn(name = "option_id")
    private ActivitySubjectOption option;

    /**
     * 问答题答案
     */
    private String textAnswer;

    private boolean finished;

}
