package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 固定班制
 */
@Data
@Entity
@Table(name = "attendance_group_shift", indexes = {
        @Index(name = "idx_group_id", columnList = "group_id")
})
@EqualsAndHashCode(callSuper = true)
public class AttendanceGroupShift extends BaseEntity {
    private Integer weekday; // 1~7,星期一到星期日， DayOfWeek

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shifts_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private AttendanceShift shifts; // 班次

    @Column(nullable = false, name = "group_id")
    private Long groupId;
}
