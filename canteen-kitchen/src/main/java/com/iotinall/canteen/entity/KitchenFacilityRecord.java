package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kitchen_facility_record", uniqueConstraints = {@UniqueConstraint(columnNames = {"record_time", "item_id", "cameraId"})})
public class KitchenFacilityRecord extends BaseEntity {
    private String img;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    @ManyToOne
    @JoinColumn(nullable = false, name = "item_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private KitchenItem item;

    /**
     * 逗号拼接
     */
    private String dutyEmployees;

    @Column(nullable = false)
    private Integer state;

    /**
     * 摄像头ID
     */
    private Long cameraId;

    private String requirements;

    private String comments;
}
