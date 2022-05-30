package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * AIBox设备
 *
 * @author loki
 * @date 2021/7/20 14:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "device_aibox")
public class DeviceAIBox extends BaseEntity {

    /**
     * 设备名称
     */
    @Column(nullable = false, length = 64)
    private String name;

    /**
     * 设备编号
     */
    @Column(name = "device_no", nullable = false, unique = true)
    private String deviceNo;

    /**
     * 租户组织ID
     */
    private Long tenantOrgId;

    /**
     * 安装位置
     */
    private String areaName;

    /**
     * 设备IP
     */
    private String ip;
}
