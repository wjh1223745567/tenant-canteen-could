package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 供应商
 **/
@Data
@Entity
@Table(name = "stock_supplier")
@EqualsAndHashCode(callSuper = true)
public class StockSupplier extends BaseEntity {
    /**
     * 供应商编码
     */
    @Column(length = 32)
    private String code;

    /**
     * 供应商名称
     */
    @Column(length = 64)
    private String name;

    /**
     * 信誉等级
     */
    @Column(precision = 19, scale = 1)
    private Integer credit;

    /**
     * 联系人
     */
    @Column(length = 16)
    private String contact;

    /**
     * 联系电话
     **/
    @Column(length = 16)
    private String contactNumber;

    /**
     * 联系人地址
     **/
    @Column(length = 128)
    private String address;

    /**
     * 供应商类型
     **/
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false, foreignKey = @ForeignKey(name = "null"))
    private StockSupplierType type;

    /**
     * 合作证明
     */
    @Column(length = 128)
    private String cooperation;

    /**
     * 是否禁用
     * true - 禁用
     * false - 未禁用
     */
    private Boolean disabled;
}
