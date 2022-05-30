package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 环境卫生
 *
 * @date 2021/03/12 17:24
 */
@Data
@Entity
@Table(name = "kitchen_env_inspect_record", uniqueConstraints = {@UniqueConstraint(columnNames = {"record_time", "item_id"})})
public class KitchenEnvInspectRecord extends BaseEntity {
    private String img;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    @ManyToOne
    @JoinColumn(nullable = false, name = "item_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private KitchenItem item;

    private Integer state;

    private String comments;

    private Long cameraId;
}
