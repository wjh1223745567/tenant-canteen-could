package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 加班申请记录
 */
@Data
@Entity
@Table(name = "work_overtime_apply", uniqueConstraints = {@UniqueConstraint(columnNames = {"empId", "thisDay"})})
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WorkOvertimeApply extends BaseEntity {

    /**
     * 加班人
     */
    private Long empId;

    /**
     * 开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDate thisDay;

    /**
     * 状态
     */
    @Column(nullable = false)
    private Integer state;

    /**
     * 审核意见
     */
    @Column
    private String auditOpinion;
}
