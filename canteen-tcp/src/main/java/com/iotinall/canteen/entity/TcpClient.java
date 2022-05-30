package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * tcp 接入客户端
 *
 * @author loki
 * @date 2021/6/25 10:46
 **/
@Data
@Entity
@Table(name = "tcp_client")
@EqualsAndHashCode(callSuper = true)
public class TcpClient extends BaseEntity {
    /**
     * 客户端唯一标志
     */
    private String clientKey;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 最近一次上线时间
     */
    private LocalDateTime lastSeen;

    /**
     * 客户端IP
     */
    private String ip;

    /**
     * 状态 0/null 不在线   1-在线
     */
    private Integer status;

    /**
     * 客户端类型
     */
    private String type;

    /**
     * 租户ID
     */
    private Long tenantOrgId;

    /**
     * 安装位置
     */
    private String areaName;
}
