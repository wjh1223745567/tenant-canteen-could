package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 商品库存
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock")
@EqualsAndHashCode(exclude = "goods", callSuper = true)
@ToString(exclude = "goods")
@JsonIgnoreProperties(value = "goods")
@Accessors(chain = true)
public class Stock extends BaseEntity {
    /**
     * 对应商品
     */
    @OneToOne(mappedBy = "stock", cascade = CascadeType.ALL)
    private StockGoods goods;

    /**
     * 库存数
     */
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return this.amount == null ? BigDecimal.ZERO : this.amount;
    }
}
