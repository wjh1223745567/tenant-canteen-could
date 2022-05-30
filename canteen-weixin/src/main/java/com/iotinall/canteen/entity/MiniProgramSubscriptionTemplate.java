package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 小程序订阅消息
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "wx_mini_program_subscription_template")
public class MiniProgramSubscriptionTemplate extends BaseEntity {
    /**
     * 微信对应消息模板ID
     */
    @Column(length = 128, name = "template_id")
    private String templateId;

    /**
     * 模板类型
     */
    private String type;

    @Column(length = 64, name = "app_id")
    private String appId;

}
