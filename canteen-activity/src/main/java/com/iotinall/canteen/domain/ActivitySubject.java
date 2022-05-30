package com.iotinall.canteen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 活动调查题目
 *
 * @author joelau
 * @date 2021/05/29 14:00
 */
@Data
@Entity
@Table(name = "activity_subject")
@ToString(exclude = {"survey", "options"})
@EqualsAndHashCode(callSuper = true, exclude = {"survey", "options"})
@JsonIgnoreProperties(value = {"survey", "options"})
public class ActivitySubject extends BaseEntity {
    /**
     * 问题类型 0-单选 1-多选 2-问答
     */
    private Integer type;

    /**
     * 问题名称
     */
    private String name;

    /**
     * 问题提示
     */
    private String tips;

    /**
     * 题目选项
     */
    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    @OrderBy("seq asc ")
    private List<ActivitySubjectOption> options;

    /**
     * 投票活动
     */
    @ManyToOne
    @JoinColumn(name = "activity_survey_id")
    private ActivitySurvey survey;

    /**
     * 排序
     */
    private Integer seq;
}
