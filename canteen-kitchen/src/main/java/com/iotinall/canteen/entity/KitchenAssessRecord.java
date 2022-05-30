package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "kitchen_assess_record", uniqueConstraints = {@UniqueConstraint(columnNames = {"empId", "typ", "beginDate", "endDate"})})
public class KitchenAssessRecord extends BaseEntity {

    private Long empId;

    private String empName; //冗余被考核人员名称

    @ManyToOne
    @JoinColumn(nullable = false, name = "item_id")
    private KitchenItem item;

    /**
     * @see com.iotinall.canteen.constant.AssessRecordTypeEnum
     */
    @Column(nullable = false)
    private Integer typ; // 考核类型 0-月度，1-季度，2-年度

    @Column(nullable = false)
    private LocalDate beginDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String comments; // 考核评语

    /**
     * 考核人
     */
    private Long auditorId;
}
