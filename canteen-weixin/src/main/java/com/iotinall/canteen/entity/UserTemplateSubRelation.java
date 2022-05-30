package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户已订阅小程序订阅消息
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "wx_user_template_sub_relation")
public class UserTemplateSubRelation extends BaseEntity {
    /**
     * 用户openId
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 微信对应消息模板ID
     */
    @OneToOne
    @JoinColumn(name = "template_id")
    private MiniProgramSubscriptionTemplate template;

}
