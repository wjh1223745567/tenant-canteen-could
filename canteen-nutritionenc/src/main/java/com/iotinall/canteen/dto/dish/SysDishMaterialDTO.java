package com.iotinall.canteen.dto.dish;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 菜品原料 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 10:28
 */
@Data
@ApiModel(value = "菜品原料")
public class SysDishMaterialDTO implements Serializable {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "菜品ID")
    private String dishId;

    @ApiModelProperty(value = "菜品名称")
    private String dishName;

    @ApiModelProperty(value = "原料ID")
    private String materialId;

    @ApiModelProperty(value = "原料名称")
    private String materialName;

    @ApiModelProperty(value = "主料还是配料")
    private Integer master;

    @ApiModelProperty(value = "数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "食物相克")
    private String restriction;
}
