package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 设备
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "device_attendance")
public class DeviceAttendance extends BaseEntity {
    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * 设备名称
     */
    @Column(nullable = false, length = 64)
    private String name;

    /**
     * 设备IP
     */
    @Column
    private String ip;

    /**
     * 设备类型 0-闸机 1-人脸识别终端  2-读卡器 3-发卡器
     */
    @Column
    private Integer type;

    /**
     * 状态 0-待激活 1-正常 2-异常
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 员工库
     */
    private Long empLib;

    /**
     * 其他库
     */
    private Long otherLib;

    /**
     * 安装位置
     */
    private Long tenantOrgId;

    /**
     * 详细位置
     */
    private String areaName;
}