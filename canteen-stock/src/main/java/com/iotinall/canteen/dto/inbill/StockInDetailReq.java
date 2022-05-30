package com.iotinall.canteen.dto.inbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 入库详情
 *
 * @author loki
 * @date 2020/05/04 16:45
 */
@Data
@ApiModel(description = "入库商品请求参数")
public class StockInDetailReq implements Serializable {
    /**
     * 货品ID
     */
    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品不能为空")
    private Long goodsId;

    /**
     * 检测报告
     * true - 有
     * false -无
     */
    @ApiModelProperty(value = "检测报告", allowableValues = "true,false")
    @NotNull(message = "检测报告不能为空")
    private boolean inspectionReport;

    /**
     * 数量
     */
    @NotNull(message = "商品数量不能为空")
    @ApiModelProperty(value = "商品数量")
    private BigDecimal amount;

    /**
     * 数量
     */
    @ApiModelProperty(value = "商品入库当时库存数量")
    @NotNull(message = "商品入库当前库存数量不能为空")
    private BigDecimal stockAmount;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    /**
     * 保质日期
     */
    @ApiModelProperty(value = "保质日期")
    private LocalDate shelfLife;

    /**
     * 货品单价
     */
    @NotNull(message = "单价不能为空")
    @ApiModelProperty(value = "商品单价")
    private BigDecimal price;
}
