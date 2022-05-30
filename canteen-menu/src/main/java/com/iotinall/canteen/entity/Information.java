package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 咨询列表
 *
 * @author WJH
 * @date 2019/11/19:22
 */
@Setter
@Getter
@Accessors(chain = true)
@Entity
@Table(name = "information")
@EqualsAndHashCode(exclude = {"type"}, callSuper = false)
@JsonIgnoreProperties(value = {"type"})
@ToString(exclude = {"type"})
public class Information extends BaseEntity {
    /**
     * 标题
     */
    @Column(nullable = false)
    private String title;

    /**
     * 点赞数
     */
    private String cover;

    /**
     * 点赞数
     */
    @Column(nullable = false)
    private Integer praiseCount;

    /**
     * 点赞人
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    private String praiseEmpId;

    @JoinColumn(name = "type_id", foreignKey = @ForeignKey(name = "null"))
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private InformationType type;

    /**
     * 是否置顶
     */
    private Boolean sticky;

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    /**
     * 是否启用
     */
    private Integer status;

    /**
     * 资讯接收组织机构，id全路径以|拼接，多个组织机构以,分割，例如 1|2,1|3
     */
    @Column(name = "receiver")
    private String receiver;

    /**
     * 资讯发布人组织机构
     */
    @Column(name = "publisher_org_id")
    private Long publisherOrgId;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 业务ID，根据业务ID判断唯一性
     */
    private String businessId;
}
