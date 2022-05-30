package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 仓库管理员
 **/
@Accessors(chain = true)
@Data
@Entity
@Table(name = "stock_warehouse_manager")
@EqualsAndHashCode(callSuper = true)
public class StockWarehouseManager extends BaseEntity {
    /**
     * 责任人ID
     */
    private Long managerId;

    /**
     * 责任人名称
     */
    private String managerName;

    /**
     * 仓库ID
     */
    private Long warehouseId;
}
