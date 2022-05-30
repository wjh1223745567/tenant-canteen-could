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
 * 传感器
 *
 * @author loki
 * @date 2021/02/22 15:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "device_sensor_data")
public class DeviceSensorData extends BaseEntity {

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性值
     */
    private String value;

    /**
     * 单位
     */
    private String unit;

    /**
     * 传感器devEUI
     */
    private String devEUI;

    /**
     * 设备设备用途 1-餐厅环境 2-后厨环境 3-仓库环境
     */
    @Column
    private Integer type;

    /**
     * 版本号，每次更新删除以前版本的项
     */
    private LocalDateTime version;
}
