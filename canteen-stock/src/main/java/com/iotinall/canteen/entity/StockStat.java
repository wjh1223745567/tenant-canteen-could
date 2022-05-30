package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存汇总
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "stock_stat")
@EqualsAndHashCode(callSuper = false)
public class StockStat extends BaseEntity {
    /**
     * 类别 1-结存 2-入库 3-出库
     *
     * @see com.iotinall.canteen.constant.Constant.STOCK_STAT_TYPE_LEFT
     */
    private Integer type;

    /**
     * 数量
     */
    private BigDecimal count;

    /**
     * 单价 存放汇总时最新的单价
     */
    private BigDecimal price;

    /**
     * 日期，保存年月
     */
    @DateTimeFormat(pattern = "yyyy-MM")
    private LocalDate date;

    /**
     * 对应商品ID
     */
    @ManyToOne
    @JoinColumn(name = "goods_id")
    private StockGoods goods;

    public BigDecimal getCount() {
        return this.count == null ? BigDecimal.ZERO : this.count;
    }

    public BigDecimal getPrice() {
        return this.price == null ? BigDecimal.ZERO : this.price;
    }
}
