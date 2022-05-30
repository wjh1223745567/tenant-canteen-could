package com.iotinall.canteen.dto.stock;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "商品详情")
public class StockAppletsDetailDTO implements Serializable {
    @ApiModelProperty(value = "商品图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String goodsImgUrl;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    @ApiModelProperty(value = "商品类型")
    private String goodsType;

    @ApiModelProperty(value = "单位")
    private String goodsUnit;

    @ApiModelProperty(value = "规格型号")
    private String goodsSpecs;

    @ApiModelProperty(value = "仓库")
    private String warehouseName;

    @ApiModelProperty(value = "仓库图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String warehouseImgUrl;

    @ApiModelProperty(value = "库存")
    private BigDecimal amount;

    @ApiModelProperty(value = "库存下限")
    private Integer lowerLimit;
}
