package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 消息发送记录
 *
 * @author loki
 * @date 2021/6/25 10:46
 **/
@Data
@Entity
@Table(name = "msg_send_record")
@EqualsAndHashCode(callSuper = true)
public class MsgSendRecord extends BaseEntity {
    /**
     * 接口客户端
     */
    private Long clientId;

    /**
     * 发送类型 tcp/websocket
     */
    private String sendType;

    /**
     * 消息内容
     */
    private String msg;
}
