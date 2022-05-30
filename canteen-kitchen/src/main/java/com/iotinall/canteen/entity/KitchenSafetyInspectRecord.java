package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kitchen_safety_inspect_record")
@EqualsAndHashCode
public class KitchenSafetyInspectRecord extends BaseEntity {
    @Column(nullable = false)
    private LocalDateTime recordTime;

    private String img;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private KitchenItem item;

    @Column(nullable = false, name = "duty_emp_id")
    private Long dutyEmpId;

    private String dutyEmpName;

    @Column(nullable = false)
    private Long auditorId;

    private String auditorName;

    @Column(nullable = false)
    private Integer state;

    private String comments;
}
