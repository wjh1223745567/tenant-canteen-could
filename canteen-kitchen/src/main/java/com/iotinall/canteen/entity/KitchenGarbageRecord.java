package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 厨余垃圾
 *
 * @date 2021/03/12 17:22
 */
@Data
@Entity
@Table(name = "kitchen_garbage_record", uniqueConstraints = {@UniqueConstraint(columnNames = {"record_time", "item_id", "cameraId"})})
@SQLDelete(sql = "update kitchen_item set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
public class KitchenGarbageRecord extends BaseEntity {
    private String img;

    /**
     * 检查区域
     */
    @ManyToOne
    @JoinColumn(nullable = false, name = "kitchen_region_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private KitchenItem kitchenRegion;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    @ManyToOne
    @JoinColumn(nullable = false, name = "item_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private KitchenItem item;

    @Column(nullable = false)
    private Integer state;

    private String comments;

    private Long cameraId;

    @Column(nullable = false)
    private Boolean deleted;
}
