package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 微信消息发送日志
 *
 * @author loki
 * @date 2021/04/16 10:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "wx_message_send_record")
public class WxMessageSendRecord extends BaseEntity {

    /**
     * 消息类型
     */
    @OneToOne
    @JoinColumn(name = "config_id")
    private WxMessageContentConfig config;

}
