package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 系统角色
 *
 * @author xin-bing
 * @date 10/16/2019 13:48
 */
@Data
@JsonIgnoreProperties(value = {"employees", "permissions"})
@ToString(exclude = {"employees"})
@EqualsAndHashCode(exclude = {"employees", "permissions"}, callSuper = false)
@Entity
@Table(name = "sys_role")
@NamedEntityGraph(name = "SysRole.Graph", attributeNodes = {@NamedAttributeNode(value = "permissions")})
public class SysRole extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String name; // 角色名称

    private Integer typ; // 类型，0-默认权限 1是系统级

    /**
     * 租户ID
     */
    @Column(nullable = false)
    private Long tenantOrgId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_roles_permissions", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "permission", referencedColumnName = "permission", columnDefinition = "varchar(32)")
    })
    private Set<SysPermission> permissions;

    @ManyToMany(mappedBy = "roles")
    @OrderBy("createTime desc ")
    private Set<OrgEmployee> employees;

}
