package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "device_passage")
public class DevicePassage extends BaseEntity {
    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * 详细位置
     */
    private String areaName;

    /**
     * 租户组织ID
     */
    private Long tenantOrgId;
}
