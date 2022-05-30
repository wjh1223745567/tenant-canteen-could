package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 考勤机人员关系表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "device_employee_relation")
public class EquipmentEmployeeRelation extends BaseEntity {
    /**
     * 设备ID
     */
    @Column
    private Long equId;

    /**
     * 人员ID
     */
    @Column
    private Long empId;

    /**
     * 同步时间
     */
    @Column
    private LocalDateTime syncTime;

    /**
     * 状态 0-未同步 1-已同步  2-无照片（未同步）
     */
    @Column
    private Integer syncStatus;
}
