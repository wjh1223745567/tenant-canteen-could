package com.iotinall.canteen.dto.goodstype;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 编辑商品类别请求参数
 *
 * @author
 * @date 2020/08/25 17:02
 */
@ApiModel(value = "编辑商品类别请求参数")
@Data
public class GoodsTypeUpdateReq {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "货品名称")
    private String name;

    @ApiModelProperty(value = "上级类别ID")
    private Long parentId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
