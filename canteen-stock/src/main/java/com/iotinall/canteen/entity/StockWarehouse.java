package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

/**
 * 仓库
 **/
@Data
@Entity
@Table(name = "stock_warehouse")
@EqualsAndHashCode(callSuper = true)
public class StockWarehouse extends BaseEntity {
    /**
     * 仓库名称
     */
    @Column(length = 64)
    private String name;

    /**
     * 位置全称
     */
    @Column(length = 128)
    private String fullName;

    /**
     * 仓库地理位置
     */
    @Column(length = 128)
    private String address;

    /**
     * 仓库图片
     */
    @Column(length = 128)
    private String imgUrl;

    /**
     * 类型 0-仓库 1-位置
     */
    private Integer type;

    /**
     * 父级
     */
    private Long pid;

    /**
     * 子集
     */
    @OneToMany(mappedBy = "pid", fetch = FetchType.EAGER)
    @OrderBy("createTime desc")
    private Set<StockWarehouse> children;

    /**
     * 仓库类型
     */
    @OneToOne
    @JoinColumn(name = "type_id")
    private StockWarehouseType warehouseType;

    /**
     * 仓库管理员
     */
    @OneToMany(mappedBy = "warehouseId")
    @NotFound(action = NotFoundAction.IGNORE)
    @OrderBy("managerId desc")
    private Set<StockWarehouseManager> managers;
}
