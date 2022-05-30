package com.iotinall.canteen.dto.stock;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存预警
 *
 * @author loki
 * @date 2020/05/06 19:40
 */
public interface TerminalStockWarningDTO {
    Long getId();

    /**
     * 下线预警 1-报警
     */
    Boolean getLowerWarn();

    /**
     * 保质期预警 1-报警
     */
    Boolean getShelfLifeWarn();

    /**
     * 货品name
     */
    String getGoodsName();

    /**
     * 规格
     */
    String getSpecs();

    /**
     * 生产日期
     */
    LocalDate getProductionDate();

    /**
     * 保质期
     */
    LocalDate getShelfLife();

    /**
     * 库存
     */
    BigDecimal getAmount();

    /**
     * 仓库位置名称
     */
    String getStructureFullName();

    /**
     * 仓库ID
     */
    Long getWarehouseId();

}
