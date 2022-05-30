package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "mess_takeout_product_type")
@SQLDelete(sql = "update mess_takeout_product_type set deleted = 1 where id = ?")
@Where(clause = " deleted = 0 ")
public class MessTakeoutProductType extends BaseEntity {
    @Column(nullable = false, length = 128)
    private String name; // 商品类别

    @ManyToOne
    @JoinColumn(name = "pid", foreignKey = @ForeignKey(name = "null"))
    private MessTakeoutProductType parent;

    private Boolean deleted;


    /**
     * 租户ID，所属哪个食堂
     */
    @Column(nullable = false, updatable = false)
    private Long tenantId;
}
