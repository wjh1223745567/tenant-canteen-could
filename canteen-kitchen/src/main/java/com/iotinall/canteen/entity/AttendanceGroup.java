package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "attendance_group")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AttendanceGroup extends BaseEntity {
    private String name;

    private Integer empCount;

    /**
     * @see com.iotinall.canteen.constant.AttendanceGroupTypeEnum
     */
    @Column(nullable = false)
    private Integer type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "groupId")
    private Set<AttendanceGroupMember> memberList; // 用户列表

    @OneToMany(mappedBy = "groupId", fetch = FetchType.EAGER)
    private Set<AttendanceGroupShift> shiftList; // 班次列表

    /**
     * 排班制列表
     */
    @OneToMany(mappedBy = "groupId", fetch = FetchType.EAGER)
    private Set<AttendanceGroupShiftSystem> shiftSystems;

    /**
     * 节假日自动排休
     */
    private Boolean holidays;

    /**
     * 加班规则
     */
    @ManyToOne
    @JoinColumn(name = "work_overtime_config_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private WorkOvertimeConfig overtimeConfig;
}
