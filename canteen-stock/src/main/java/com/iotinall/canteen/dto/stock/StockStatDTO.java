package com.iotinall.canteen.dto.stock;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存汇总
 *
 * @author loki
 * @date 2020/09/12 11:00
 */
@Data
@ApiModel(description = "库存汇总")
public class StockStatDTO implements Serializable {
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "货品编号")
    private String code;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "商品规格")
    private String specs;

    @ApiModelProperty(value = "商品单位")
    private String unit;

    @ApiModelProperty(value = "参考单价")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal price;

    @ApiModelProperty(value = "商品类别名称")
    private String goodsTypeName;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "上月结存")
    private StockStatDetailDTO preMonthTotal;

    @ApiModelProperty(value = "本月入库")
    private StockStatDetailDTO currentMonthStockIn;

    @ApiModelProperty(value = "本月出库")
    private StockStatDetailDTO currentMonthStockOut;

    @ApiModelProperty(value = "本月结存")
    private StockStatDetailDTO currentMonthTotal;
}
