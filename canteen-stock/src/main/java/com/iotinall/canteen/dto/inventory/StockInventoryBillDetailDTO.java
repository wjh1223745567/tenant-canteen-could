package com.iotinall.canteen.dto.inventory;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 盘点单据明细
 *
 * @date 2020/09/24 9:19
 */
@Data
public class StockInventoryBillDetailDTO implements Serializable {
    @ApiModelProperty(value = "货品ID")
    private Long goodsId;

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

    @ApiModelProperty(value = "库存下线")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal lowerLimit;

    @ApiModelProperty(value = "货品类别ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "货品类别名称")
    private String goodsTypeName;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String warehouseImgUrl;

    @ApiModelProperty(value = "盘点数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "库存数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal stockAmount;
}
