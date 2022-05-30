package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 购物车
 * @author WJH
 * @date 2019/11/2311:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table
public class ShoppingCart extends BaseEntity {

    @OneToMany(mappedBy = "shoppingCart")
    private List<ShoppingCartProduct> cartProducts;

    @Column(name = "emp_id")
    private Long orgEmployeeId;

}
