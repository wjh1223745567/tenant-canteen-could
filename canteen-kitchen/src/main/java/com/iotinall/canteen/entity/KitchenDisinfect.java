package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 消毒管理
 */
@Data
@Entity
@Table(name = "kitchen_disinfect", uniqueConstraints = {@UniqueConstraint(columnNames = {"record_time", "cameraId", "item_id"})})
public class KitchenDisinfect extends BaseEntity {
    /**
     * 消毒时间
     */
    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    private String img;

    /**
     * 消毒区域
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private KitchenItem item;

    /**
     * 检查人
     */
    private Long auditorId;

    /**
     * 摄像头ID
     */
    private Long cameraId;

    /**
     * 检查状态
     */
    @JoinColumn
    private Integer state;

    private String comments; // 备注

    private String requirements;//检查要求
}
