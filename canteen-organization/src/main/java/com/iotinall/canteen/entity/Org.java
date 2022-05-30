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
 * 组织
 *
 * @author xin-bing
 * @date 10/10/2019 20:58
 */
@NamedEntityGraph(name = "org.findOrgTree",
        attributeNodes = @NamedAttributeNode(value = "children", subgraph = "childrenGraph"),
        subgraphs = @NamedSubgraph(name = "childrenGraph", attributeNodes = @NamedAttributeNode("children"))
)
@Data
@Entity
@Table(name = "org")
@ToString(exclude = {"employees", "children"})
@JsonIgnoreProperties(value = {"employees", "children"})
@EqualsAndHashCode(exclude = {"employees", "children"}, callSuper = false)
public class Org extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String name; // 组织名称

    private Long parentId; // 父组织

    @Column(nullable = false)
    private Integer empCount; // 组织人数

    @Column(nullable = false)
    private Integer sort; // 排序

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    @OrderBy("sort asc, id asc")
    private Set<Org> children; // 子组织

    @OneToMany(mappedBy = "org")
    private Set<OrgEmployee> employees; // 组织员工

    /**
     * 父亲组织机构完整ID，以|拼接，例如1|2|3
     */
    @Column(name = "parent_org_full_id")
    private String parentOrgFullId;

    @Column(columnDefinition = "bit default 0 ")
    private Boolean deleted;

    /**
     * 部门编码
     */
    private String orgCode;

    /**
     * 租户ID
     */
    private Long tenantId;
}
