package com.iotinall.canteen.dto.goods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.protocol.NutritionDto;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 添加货品请求参数
 *
 * @date 2020/09/15 15:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "添加货品请求")
public class StockGoodsAddReq extends NutritionDto implements Serializable {
    @ApiModelProperty(value = "货品编号", required = true)
    private String code;

    @ApiModelProperty(value = "货品名称", required = true)
    @NotBlank(message = "货品名称不能为空")
    private String name;

    @ApiModelProperty(value = "货品别名")
    private String nickname;

    @ApiModelProperty(value = "货品图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "货品规格", required = true)
    //@NotBlank(message = "填写货品规格型号")
    private String specs;

    private BigDecimal quality;

    @ApiModelProperty(value = "货品单位", required = true)
    @NotBlank(message = "填写货品单位")
    private String unit;

    @ApiModelProperty(value = "参考单价", required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "库存下线", required = true)
    private BigDecimal lowerLimit;

    @ApiModelProperty(value = "货品类别")
    @NotNull(message = "商品类别不能为空")
    private Long goodsTypeId;

    @ApiModelProperty(value = "仓库")
    @NotNull(message = "仓库不能为空")
    private Long warehouseId;

    private String dishMaterialId;
}
