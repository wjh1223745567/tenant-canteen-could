package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 系统权限表
 * @author xin-bing
 * @date 10/18/2019 11:37
 */
@EqualsAndHashCode(callSuper = true, exclude = {"children"})
@Data
@Entity
@NamedEntityGraph(name = "SysPermission.loadPermissionTree",
        attributeNodes = @NamedAttributeNode(value = "children", subgraph = "childrenGraph"),
        subgraphs = @NamedSubgraph(name = "childrenGraph", attributeNodes = @NamedAttributeNode("children"))
)
@Table(name = "sys_permission", indexes = {
        @Index(name = "permission", columnList = "permission")
})
public class SysPermission extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String name; // 权限名称

    @Column(nullable = false, columnDefinition = "varchar(64)")
    private String permission; // 权限值

    private Long pid; // 上级权限

    /**
     * @see com.iotinall.canteen.constant.SysPermissionType
     */
    private Integer permissionType;

    @Column
    private Integer sort;

    @OneToMany(mappedBy = "pid", fetch = FetchType.LAZY)
    @OrderBy("sort")
    private List<SysPermission> children;
}
