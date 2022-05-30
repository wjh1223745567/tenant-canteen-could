package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * 排班制
 */

@Data
@Entity
@Table(name = "attendance_group_shift_system", uniqueConstraints = {
        @UniqueConstraint(name = "groupId_employee_unique", columnNames = {"group_id", "empId"})})
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AttendanceGroupShiftSystem extends BaseEntity {

    @Column(nullable = false, name = "group_id")
    private Long groupId;

    @OneToMany(mappedBy = "shiftSystemId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AttendanceGroupShiftSystemDate> shiftSystemDateList;

    /**
     * 排版人
     */
    private Long empId;
}
