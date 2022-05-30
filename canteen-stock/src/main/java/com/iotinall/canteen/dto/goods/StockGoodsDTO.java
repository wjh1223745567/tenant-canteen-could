package com.iotinall.canteen.dto.goods;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.protocol.NutritionDto;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.warehouse.StockWarehouseDTO;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品列表
 */
@Data
@ApiModel(description = "返回商品结果")
@EqualsAndHashCode(callSuper = true)
public class StockGoodsDTO extends NutritionDto implements Serializable {
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "货品编号")
    private String code;

    @ApiModelProperty(value = "货品名称")
    private String name;

    @ApiModelProperty(value = "货品图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "货品规格")
    private String specs;

    @ApiModelProperty(value = "货品单位")
    private String unit;

    @ApiModelProperty(value = "参考单价")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal price;

    @ApiModelProperty(value = "货品类别ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "货品类别名称")
    private String goodsTypeName;

    @ApiModelProperty(value = "仓库")
    private StockWarehouseDTO warehouse;

    private BigDecimal quality;

    @ApiModelProperty(value = "库存下线")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal lowerLimit;

    @ApiModelProperty(value = "商品对应库存")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal stockAmount;

    @ApiModelProperty(value = "备注")
    private String remark;
}
