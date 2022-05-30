package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * 结果表，包括考勤结果，以及各种结果，汇总在一起
 */
@Data
@Entity
@Table(name = "kitchen_reuslt", indexes = {
        @Index(name = "idx_record_id", columnList = "recordId, itemType", unique = true)
})
public class KitchenResult extends BaseEntity {
    @Column(nullable = false)
    private String itemType;

    private Integer state;

    private LocalDate resultDate;

    @Column(nullable = false)
    private Long recordId;
}
