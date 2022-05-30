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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 题目选项
 *
 * @author joelau
 * @date 2021/05/29 14:00
 */
@Data
@Entity
@Table(name = "activity_subject_option")
@ToString(exclude = "subject")
@EqualsAndHashCode(callSuper = true, exclude = "subject")
@JsonIgnoreProperties(value = "subject")
public class ActivitySubjectOption extends BaseEntity {
    /**
     * 选项名
     */
    @Column(nullable = false)
    private String name;

    /**
     * 选项排序
     */
    private Integer seq;

    /**
     * 题目
     */
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private ActivitySubject subject;

}
