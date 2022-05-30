package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 库存明细变动记录
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_detail_change_record")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockDetailChangeRecord extends BaseEntity {
    /**
     * 对应库存详情
     */
    @OneToOne
    @JoinColumn(name = "stock_detail_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private StockDetail stockDetail;

    /**
     * 变动数量
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    /**
     * 变动前库存
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal beforeAmount;

    /**
     * 变动后库存
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal afterAmount;

    /**
     * 0-添加  1-减少
     */
    private Integer type;

    /**
     * 扣减顺序
     */
    private Integer seq;

    /**
     * 对应操作的单据明细
     */
    @OneToOne
    @JoinColumn(name = "bill_detail_id")
    private StockBillDetail billDetail;
}
