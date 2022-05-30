package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 光盘行动消息推送配置
 *
 * @author loki
 * @date 2021/7/7 10:38
 **/
@Data
@Entity
@Table(name = "empty_plate_msg_config")
@EqualsAndHashCode(callSuper = false)
public class EmptyPlateMsgConfig extends BaseEntity {
    /**
     * 推送客户端ID
     */
    private Long clientId;

    /**
     * 摄像机
     */
    private Long cameraId;

    /**
     * 名称
     */
    private String name;
}
