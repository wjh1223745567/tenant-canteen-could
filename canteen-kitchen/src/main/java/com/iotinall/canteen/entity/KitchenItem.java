package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * 考评等级
 */
@Entity
@Table(name = "kitchen_item")
@SQLDelete(sql = "update kitchen_item set deleted = 1 where id =?")
@Where(clause = "deleted = 0")
@Data
@ToString(exclude = "dutyEmployeeList")
@EqualsAndHashCode(exclude = "dutyEmployeeList")
@JsonIgnoreProperties(value = "dutyEmployeeList")
public class KitchenItem extends BaseEntity {
    @Column(nullable = false, length = 20)
    private String groupCode;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer seq;

    /**
     * 责任人
     */
    @OneToMany(mappedBy = "item")
    private Set<KitchenItemEmployee> dutyEmployeeList;

    /**
     * 摄像头ID，多个已逗号拼接
     */
    private String cameraIds;

    /**
     * 检查时间，LocalDateTime格式用逗号分隔
     */
    //@Column(nullable = false)
    private String checkTime;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    private String requirements;

    private String comment;
}
