package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

/**
 * 打卡记录详情
 *
 * @author loki
 * @date 2021/01/07 16:11
 */
@Data
@Entity
@Table(name = "attendance_record_detail")
@EqualsAndHashCode(callSuper = true)
public class AttendanceRecordDetail extends BaseEntity {

    /**
     * 打卡时间
     */
    @Column(columnDefinition = "time")
    private LocalTime punchTime;

    /**
     * 打卡照片
     */
    private String punchImg;

    /**
     * 打卡体温
     */
    private Float punchTemperature;

    private Long shiftRecordId;
}
