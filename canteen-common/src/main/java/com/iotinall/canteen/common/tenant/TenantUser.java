package com.iotinall.canteen.common.tenant;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 租户表，对应数据库表
 * storehouse库使用，其他库为空
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tenant_user")
@Accessors(chain = true)
public class TenantUser extends BaseEntity {

    /**
     * 租户名称
     */
    @Column(length = 80, nullable = false)
    private String name;

    /**
     * 租户CODE
     */
    @Column(length = 20, nullable = false, unique = true)
    private String code;

    /**
     * 数据库类型
     */
    private Integer type;

    /**
     * 数据库连接 IP：端口
     */
    @Column(nullable = false)
    private String sqlUrl;

    @Column(nullable = false)
    private String sqlUsername;

    @Column(nullable = false)
    private String sqlPassword;

}
