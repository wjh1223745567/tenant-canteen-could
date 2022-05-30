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
@Table(name = "wx_message_send_record_detail")
public class WxMessageSendRecordDetail extends BaseEntity {

    /**
     * 消息类型
     */
    @JoinColumn(name = "record_id")
    @OneToOne
    private WxMessageSendRecord record;

    /**
     * 接收人openId
     */
    private String openId;

    /**
     * 消息内容
     */
    private String content;

}
