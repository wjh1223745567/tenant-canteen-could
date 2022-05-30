package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 人脸设备
 *
 * @author bingo
 * @date 11/25/2019 14:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "device_camera")
public class DeviceCamera extends BaseEntity {

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
     * 最后一张图片
     */
    private String imgUrl;

    /**
     * 默认图片
     */
    @Column(length = 350)
    private String defaultImg;

    /**
     * 安装位置
     */
    private String areaName;

    /**
     * 租户组织ID
     */
    private Long tenantOrgId;
}
