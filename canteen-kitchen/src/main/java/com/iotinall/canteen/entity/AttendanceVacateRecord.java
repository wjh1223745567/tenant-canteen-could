package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 请假记录
 */
@Data
@Entity
@Table(name = "attendance_vacate_record")
@EqualsAndHashCode(callSuper = true)
public class AttendanceVacateRecord extends BaseEntity {

    private Long empId; // 所属员工

    private String empName; //冗余员工名称，方便查询

    private LocalDateTime beginTime; //开始时间

    private LocalDateTime endTime; // 结束时间

    private Integer state; // 状态 0-等待审批 1-审批通过 2-审批拒绝

    private String reason; // 请假缘由

    private String auditOpinion;

    private Long auditorId;

    private LocalDateTime auditTime; // 审核时间
}
