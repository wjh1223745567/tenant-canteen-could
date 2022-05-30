package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 库存预警
 * 1、库存下限预警
 * 2、库存保质期预警
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_warning")
@EqualsAndHashCode(callSuper = true)
public class StockWarning extends BaseEntity {
    /**
     * 库存下限预警 0-未预警 1-预警
     */
    private Boolean lowerLimitWarning;

    /**
     * 保质期预警 0-未预警 1-预警
     */
    private Boolean shelfLifeWarning;

    /**
     * 对应库存
     */
    @OneToOne
    @JoinColumn(name = "goods_id", foreignKey = @ForeignKey(name = "null"))
    private StockGoods goods;
}
