package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 加班记录
 */
@Data
@Entity
@Table(name = "work_overtime_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WorkOvertimeRecord extends BaseEntity {

    /**
     * 加班人
     */
    private Long empId;

    /**
     * 班组信息
     */
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private AttendanceGroup group;

    /**
     * 加班开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 从申请直接添加时保存ID
     */
    private Long applyId;

    /**
     * 加班结束时间
     */
    @Column
    private LocalDateTime endTime;

    /**
     * 加班时长
     */
    @Column
    private Long overtime;
}
