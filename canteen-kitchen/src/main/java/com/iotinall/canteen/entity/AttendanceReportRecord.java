package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * 考勤设备上报记录记录
 */
@Data
@Entity
@Table(name = "attendance_report_record")
@EqualsAndHashCode(callSuper = false)
public class AttendanceReportRecord extends BaseEntity {
    private Long empId;

    private LocalDate recordDate;

    private String punchImg;

    private Float temperature;
}
