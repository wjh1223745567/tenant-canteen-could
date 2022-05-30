package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 发布外带外购商品信息
 * @author WJH
 * @date 2019/11/2611:05
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Accessors(chain = true)
@Table(name = "mess_takeout_product_stock")
@SQLDelete(sql = "update mess_takeout_product_stock set deleted = 1 where id = ?")
@Where(clause = " deleted = 0 ")
public class MessTakeoutProductStock extends BaseEntity {

    @OneToOne
    @JoinColumn(nullable = false, name = "mess_product_id", foreignKey = @ForeignKey(name = "null"))
    private MessTakeoutProductInfo messProduct;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(nullable = false, name = "product_type_id", foreignKey = @ForeignKey(name = "null"))
    private MessTakeoutProductType productType;

    /**
     * 库存数量
     */
    @Column(nullable = false)
    private Integer count;

    /**
     * 限购数量
     */
    @Column(nullable = false)
    @Min(0)
    @Max(50000)
    private Integer limitCount;

    /**
     * 卖出数量
     */
    @Column(nullable = false)
    private Integer salesVolume;

    /**
     * 零售价格
     */
    @Column(nullable = false, columnDefinition = "decimal(10,2) not null")
    private BigDecimal price;

    /**
     * 状态上架下架
     */
    private Boolean state;

    private Boolean deleted;

    /**
     * 是否置顶
     */
    private Boolean top;

    @Column(nullable = false, updatable = false)
    private Long tenantId;
}
