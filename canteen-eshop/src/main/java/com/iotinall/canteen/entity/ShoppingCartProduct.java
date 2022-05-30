package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 购物车中商品
 * @author WJH
 * @date 2019/11/2311:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table
@Accessors(chain = true)
public class ShoppingCartProduct extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", foreignKey = @ForeignKey(name = "null"))
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "mess_product_id", foreignKey = @ForeignKey(name = "null"))
    private MessTakeoutProductStock messProduct;

    /**
     * 数量
     */
    private Integer count;


}
