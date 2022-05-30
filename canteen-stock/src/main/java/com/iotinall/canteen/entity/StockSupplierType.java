package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 供应商类型
 */
@Data
@Entity
@Table(name = "stock_supplier_type")
@EqualsAndHashCode(callSuper = true)
public class StockSupplierType extends BaseEntity {
    /**
     * 类型名称
     */
    @Column(length = 64)
    private String name;
}
