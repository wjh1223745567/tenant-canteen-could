package com.iotinall.canteen.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 排班日期
 */
@Data
@Entity
@IdClass(AttendanceGroupShiftSystemDate.DateSystemKey.class)
@Table(name = "attendance_group_shift_system_date")
@Accessors(chain = true)
public class AttendanceGroupShiftSystemDate {

    @Id
    @JoinColumn(name = "shift_system_id", nullable = false)
    private Long shiftSystemId;

    @Id
    @Column(nullable = false)
    private LocalDate date;

    /**
     * 班次ID 为空不上班
     */
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private AttendanceShift shift;

    @Data
    public static class DateSystemKey implements Serializable {

        @Column(nullable = false)
        private Long shiftSystemId;

        @Column(nullable = false)
        private LocalDate date;

    }
}
