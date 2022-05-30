package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 仓库类型
 **/
@Data
@Entity
@Table(name = "stock_warehouse_type")
@EqualsAndHashCode(callSuper = true)
public class StockWarehouseType extends BaseEntity {
    private Long warehouseId;

    /**
     * 仓库类型名称
     */
    @Column(length = 64)
    private String name;
}
