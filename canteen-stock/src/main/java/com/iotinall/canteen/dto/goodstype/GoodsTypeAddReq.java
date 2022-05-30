package com.iotinall.canteen.dto.goodstype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加商品类别请求参数
 */
@ApiModel(value = "添加商品类别请求参数", description = "添加商品类别请求参数")
@Data
public class GoodsTypeAddReq {
    @ApiModelProperty(value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty(value = "父节点ID")
    private Long parentId;

    @ApiModelProperty(value = "备注")
    private String remark;
}


