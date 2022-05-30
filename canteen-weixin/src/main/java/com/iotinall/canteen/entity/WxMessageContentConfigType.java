package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "wx_message_content_config_type")
public class WxMessageContentConfigType extends BaseEntity {
    /**
     * 内容
     */
    @Column(nullable = false)
    private String content;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 是否开启
     * true：开启
     * false: 关闭
     */
    @Column(nullable = false)
    private Boolean open;

    /**
     * 跳转路径
     */
    private String redirect;
}