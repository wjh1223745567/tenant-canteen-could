package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 库存明细
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_detail", uniqueConstraints = {
        @UniqueConstraint(name = "bill_goods_unique", columnNames = {"bill_detail_id", "goods_id"})
})
@EqualsAndHashCode(callSuper = true)
public class StockDetail extends BaseEntity {
    /**
     * 冗余商品,方便查询
     */
    @ManyToOne
    @JoinColumn(name = "goods_id", foreignKey = @ForeignKey(name = "null"))
    private StockGoods goods;

    /**
     * 对应商品入库单据明细
     */
    @OneToOne
    @JoinColumn(name = "bill_detail_id", foreignKey = @ForeignKey(name = "null"))
    private StockBillDetail billDetail;

    /**
     * 库存数量
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;
}
