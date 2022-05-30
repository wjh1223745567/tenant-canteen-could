package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table
@Accessors(chain = true)
public class TenantSubOrgInfo extends BaseEntity {

    @Column(nullable = false, length = 60)
    private String name;

    /**
     * 关联食堂组织ID,不可以修改
     */
    @Column(nullable = false, unique = true, updatable = false)
    private Long tenantOrgId;

    /**
     * 容纳人数
     */
    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String address;

    /**
     * 经度
     */
    @Column(nullable = false)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Column(nullable = false)
    private BigDecimal latitude;

    /**
     * 面向人群
     */
    private String forTheCrowd;

    /**
     * 联系人
     */
    private String contactPerson;

    @Column(length = 20)
    private String phone;

}
