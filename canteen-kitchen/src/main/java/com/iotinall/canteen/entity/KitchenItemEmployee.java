package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 责任人
 */
@Data
@Entity
@Table(name = "kitchen_item_employee")
@ToString(exclude = "item")
@EqualsAndHashCode(exclude = "item")
@JsonIgnoreProperties(value = "item")
public class KitchenItemEmployee extends BaseEntity {
    /**
     * 检查项、考核项等id
     */
    @ManyToOne
    private KitchenItem item;

    /**
     * 人员ID
     */
    private Long empId;

    /**
     * 冗余人员名称,便于查询
     */
    private String empName;
}
