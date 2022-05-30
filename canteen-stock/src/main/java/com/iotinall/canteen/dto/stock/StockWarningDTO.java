package com.iotinall.canteen.dto.stock;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存预警
 *
 * @author loki
 * @date 2020/05/06 19:40
 */
@Data
public class StockWarningDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "下限预警 true -报警")
    private Boolean lowerWarn;

    @ApiModelProperty(value = "保质期预警 true -报警")
    private Boolean shelfLifeWarn;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品code")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String goodsImgUrl;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "规格")
    private String specs;

    @ApiModelProperty(value = "下限")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal lowerLimit;

    @ApiModelProperty(value = "货品类别名称")
    private String typeName;

    @ApiModelProperty(value = "库存数")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String warehouseNameImgUrl;
}