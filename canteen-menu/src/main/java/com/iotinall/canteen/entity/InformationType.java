package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * 资讯类型
 *
 * @author WJH
 * @date 2019/11/19:23
 */
@Setter
@Getter
@Accessors(chain = true)
@Entity
@Table(name = "information_type")
@EqualsAndHashCode(exclude = {"informations"}, callSuper = false)
@JsonIgnoreProperties(value = {"informations"})
@ToString(exclude = {"informations"})
public class InformationType extends BaseEntity {
    /**
     * 类型
     */
    @Column(nullable = false)
    private String name;

    private Integer infoCount;
    /**
     * 状态
     */
    private Boolean status;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Information> informations;

    /**
     * 绑定的组织机构
     */
    @Column(name = "bind_org", columnDefinition = "text")
    private String bindOrg;

    /**
     * 可以查看资讯的组织机构，与bindOrg的区别是保存所有的子集
     */
    @Column(name = "receive_org", columnDefinition = "text")
    private String receiveOrg;
}
