package com.iotinall.canteen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 活动调查(投票活动/问卷调查)
 *
 * @author joelau
 * @date 2021/05/29 14:00
 */
@Data
@Entity
@Table(name = "activity_survey")
@ToString(exclude = "subjects")
@EqualsAndHashCode(callSuper = true, exclude = "subjects")
@JsonIgnoreProperties(value = "subjects")
public class ActivitySurvey extends BaseEntity {
    /**
     * 类型 0-投票活动 1-问卷调查
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 投票活动、问卷调查名称
     */
    @Column(nullable = false)
    private String title;

    /**
     * 活动介绍
     */
    private String description;
    /**
     * 开始日期
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * 截止日期
     */
    @Column(nullable = false)
    private LocalDate endDate;

    /**
     * 发布范围/组织部门ID，由逗号分隔
     */
    //勿删
//    @Column(nullable = false)
    private String orgIdList;

    /**
     * 发布范围/组织部门ID，由逗号分隔(只显示用户选的)
     */
    private String originalOrgIdList;

    /**
     * 是否置顶
     */
    @Column(nullable = false)
    private Boolean sticky;

    /**
     * 状态（禁用/启用）
     */
    @Column(nullable = false)
    private Boolean state;

    /**
     * 题目（活动内容）
     */
    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER)
    @OrderBy("seq asc ")
    private Set<ActivitySubject> subjects;

    /**
     * 活动或者调查题目总数
     */
    private Integer subjectNumber;

    /**
     * 活动封面
     */
    private String imgUrl;
}
