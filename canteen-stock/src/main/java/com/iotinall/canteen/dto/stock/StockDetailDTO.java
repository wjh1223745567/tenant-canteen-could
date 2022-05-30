package com.iotinall.canteen.dto.stock;

import com.iotinall.canteen.dto.goods.StockGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 库存明细
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@ApiModel(value = "库存明细")
public class StockDetailDTO implements Serializable {
    @ApiModelProperty(value = "入库单据号")
    private String billNo;

    private Long billDetailId;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "检测报告 true - 有 false -无")
    private Boolean inspectionReport;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "保质日期")
    private LocalDate shelfLife;

    @ApiModelProperty(value = "库存数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "入库日期")
    private LocalDate stockInDate;

    @ApiModelProperty(value = "货品")
    private StockGoodsDTO stockGoodsDTO;

    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
