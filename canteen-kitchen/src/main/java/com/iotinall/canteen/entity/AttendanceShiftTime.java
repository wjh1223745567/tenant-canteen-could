package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * 班次时间设置
 */
@Data
@Entity
@Table(name = "attendance_shift_time")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AttendanceShiftTime extends BaseEntity {

    /**
     * 上班时间
     */
    @Column(nullable = false)
    private LocalTime workingHours;

    /**
     * 上班开始打卡时间
     */
    @Column(nullable = false)
    private LocalTime workingHoursStart;

    /**
     * 上班结束打卡时间
     */
    @Column(nullable = false)
    private LocalTime workingHoursEnd;

    /**
     * 下班打卡时间
     */
    @Column(nullable = false)
    private LocalTime offWorkTime;

    /**
     * 下班开始打卡时间
     */
    @Column(nullable = false)
    private LocalTime offWorkTimeStart;

    /**
     * 下班结束打卡时间
     */
    @Column(nullable = false)
    private LocalTime offWorkTimeEnd;

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    private AttendanceShift shift;
}
