package com.iotinall.canteen.dto.messprod;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Data
@ApiModel(description = "查询菜品条件")
public class MessProductQueryCriteria {
    @ApiModelProperty(value = "菜品名称")
    private String name;
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
    @ApiModelProperty(value = "用途")
    private Integer useFor; // 用途
    @ApiModelProperty(value = "食用类别")
    private Integer mealType;
    @ApiModelProperty(value = "菜品分类,多个逗号拼接")
    private String cuisineIds;
}