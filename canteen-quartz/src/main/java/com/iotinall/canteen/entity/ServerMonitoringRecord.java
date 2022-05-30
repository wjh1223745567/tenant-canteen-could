package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务器监听记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "server_monitoring_record")
public class ServerMonitoringRecord extends BaseEntity {

    /**
     * 服务器服务器下线还是上线
     */
    @Column
    private String name;

    /**
     * 服务器名称
     */
    @Column
    private String sysName;

}
