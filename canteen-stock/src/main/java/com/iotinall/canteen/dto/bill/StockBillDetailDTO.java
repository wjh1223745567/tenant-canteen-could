package com.iotinall.canteen.dto.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.goods.StockGoodsDTO;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 出入库明细（采购入库、领用出库、领用退库）
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@ApiModel(description = "出入库明细")
public class StockBillDetailDTO implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "对应货品信息")
    private StockGoodsDTO stockGoods;

    @ApiModelProperty(value = "入库、出库数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "实际入库、出库数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal realAmount;

    @ApiModelProperty(value = "单价")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal price;

    @ApiModelProperty(value = "检测报告 true - 有 false -无")
    private Boolean inspectionReport;

    @ApiModelProperty(value = "是否已经验收 true - 是 false -否")
    private Boolean acceptance;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "保质日期")
    private Integer shelfLife;

    @ApiModelProperty(value = "保质期单位 0-年 1-月 2-日")
    private Integer shelfLifeUnit;

    @ApiModelProperty(value = "保质日期,年-月-日")
    private LocalDate shelfLifeDate;

    @ApiModelProperty(value = "库存数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal stockAmount;

    @ApiModelProperty(value = "图片（入库或者出库）")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "单据日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "商品在该批次剩余的数量，目前只针对入库单")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal leftAmount;

    private Integer status;
}
