package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 传感器
 *
 * @author loki
 * @date 2021/02/22 15:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "device_sensor")
public class DeviceSensor extends BaseEntity {
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
     * 设备设备用途 1-餐厅环境 2-后厨环境 3-仓库环境
     */
    @Column
    private Integer type;

    /**
     * 状态 0-待激活 1-正常 2-异常
     */
    @Column
    private Integer status;

    /**
     * 实时数据
     */
    @Column(columnDefinition = "text")
    private String data;

}
