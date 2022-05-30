package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 外购外带产品信息
 * @author WJH
 * @date 2019/11/2213:46
 */
@Data
@Entity
@Table
@Accessors(chain = true)
@ToString(exclude = "messTakeoutOrder")
@EqualsAndHashCode(exclude  ="messTakeoutOrder", callSuper = false)
@JsonIgnoreProperties(value = "messTakeoutOrder")
public class  MessTakeoutOrderDetail extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "null"))
    private MessTakeoutProductStock messProduct;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "null"))
    private MessTakeoutOrder messTakeoutOrder;

    /**
     * 产品数量
     */
    private Integer count;

    /**
     * 购买时价格
     */
    private BigDecimal buyAmount;
}
