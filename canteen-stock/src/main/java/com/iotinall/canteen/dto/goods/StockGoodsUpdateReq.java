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
import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "修改货品请求")
public class StockGoodsUpdateReq extends NutritionDto {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "货品编号", required = true)
    @NotBlank(message = "填写货品编号")
    private String code;

    @ApiModelProperty(value = "货品名称", required = true)
    @NotBlank(message = "填写货品名称")
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
    @NotNull(message = "填写货品单价")
    private BigDecimal price;

    @ApiModelProperty(value = "库存下线", required = true)
    @NotNull(message = "填写库存下线")
    private BigDecimal lowerLimit;

    @ApiModelProperty(value = "货品类别")
    @NotNull(message = "请选择货品类别")
    private Long goodsTypeId;

    @ApiModelProperty(value = "仓库")
    @NotNull(message = "请选择仓库")
    private Long warehouseId;

    private String dishMaterialId;

}
