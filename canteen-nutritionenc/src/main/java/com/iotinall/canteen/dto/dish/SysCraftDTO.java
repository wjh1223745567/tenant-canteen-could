package com.iotinall.canteen.dto.dish;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜品工艺
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@Data
@ApiModel(value = "菜品工艺")
public class SysCraftDTO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "工艺名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;
}
