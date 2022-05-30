package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * 考勤班次
 */
@Entity
@Table(name = "attendance_shift")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AttendanceShift extends BaseEntity {

    @Column(nullable = false)
    private String name;

    /**
     * 时间组
     */
    @OneToMany(mappedBy = "shift", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<AttendanceShiftTime> shiftTimes;

    /**
     * 是否启用宽松时间
     */
    @Column(nullable = false)
    private Boolean looseTime;

    /**
     * 可以晚到时间 MIN
     */
    private Integer canBeLate;

    /**
     * 可以早退
     */
    private Integer canLeaveEarly;

    /**
     * 是否启用严重迟到
     */
    @Column(nullable = false)
    private Boolean maxBeginTimeEnable;

    /**
     * 严重迟到时间
     */
    private Integer maxBeginTime;

    /**
     * 是否启用旷工迟到
     */
    @Column(nullable = false)
    private Boolean maxEndTimeEnable;
    /**
     * 旷工迟到时间
     */
    private Integer maxEndTime; //最晚打卡时间，超过该时间，记为缺卡 单位：分钟 超过该时间为缺卡
}
