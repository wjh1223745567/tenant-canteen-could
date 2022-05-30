package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 租户组织架构
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table
@Accessors(chain = true)
public class TenantOrganization extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String name;

    /**
     * 类型，后厨，库存，餐厅。
     * @see TenantOrganizationTypeEnum
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 对应数据库KEY TenantUser 表code 字段
     */
    @Column(length = 50, nullable = false)
    private String dataSourceKey;

    /**
     * 父级节点
     */
    private Long pid;

    /**
     * |分割
     */
    @Column(columnDefinition = "text")
    private String allPids;

    /**
     * 层级
     */
    @Column(nullable = false)
    private Integer level;

}
