package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "attendance_group_member", indexes = {
        @Index(name = "idx_emp_id", columnList = "emp_id"),
        @Index(name = "idx_group_id", columnList = "group_id")
})
public class AttendanceGroupMember extends BaseEntity{

    @Column(nullable = false, name = "group_id")
    private Long groupId; // 组id

    @Column(nullable = false, name = "emp_id")
    private Long empId; // 员工id
}
