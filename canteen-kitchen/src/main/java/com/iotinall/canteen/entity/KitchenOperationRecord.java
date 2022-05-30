package com.iotinall.canteen.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 厨房操作记录
 */
@Data
@Entity
@Table(name = "kitchen_operation_record")
public class KitchenOperationRecord extends BaseEntity {
    @Column(nullable = false)
    private Integer mealType; // 餐次类型

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private KitchenItem item; // 清洗类型

    /**
     * ItemType
     */
    @Column(nullable = false)
    private String itemType; // Constants

    @Column(nullable = false)
    private LocalDateTime recordTime; // 操作时间

    @Column(name = "duty_emp_id", nullable = false)
    private Long dutyEmpId; // 责任人

    private String dutyEmpName;

    private String auditorName;

    @Column(name = "auditor_id", nullable = false)
    private Long auditorId; // 检查人

    @JoinColumn(nullable = false)
    private Integer state; // 检查状态

    private String comments; // 备注

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; //记录照片
}
