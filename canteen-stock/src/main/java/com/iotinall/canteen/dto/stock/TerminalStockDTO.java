package com.iotinall.canteen.dto.stock;

import lombok.Data;

/**
 * 跑马灯显示库存信息
 *
 * @author loki
 * @date 2020/05/09 14:00
 */
@Data
public class TerminalStockDTO {
    private Long id;
    /**
     * 货品名称
     */
    private String goodsName;
    /**
     * 库存
     */
    private String stockAmount;
    /**
     * 仓库
     */
    private String structureFullName;
    /**
     * 生产日期
     */
    private String productionDate;
    /**
     * 保质日期
     */
    private String shelfLife;
    /**
     * 状态 0 -库存正常 1-上限预警 2-下限预警 3-保质期预警
     */
    private String statusName;

    /**
     * 下线预警 1-报警
     */
    private Boolean lowerWarn;

    /**
     * 保质期预警 1-报警
     */
    private Boolean shelfLifeWarn;
}
