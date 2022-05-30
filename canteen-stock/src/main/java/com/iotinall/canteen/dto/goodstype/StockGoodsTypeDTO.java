package com.iotinall.canteen.dto.goodstype;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货品类型
 */
@ApiModel(value = "货品类型")
@Data
public class StockGoodsTypeDTO {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;
}
