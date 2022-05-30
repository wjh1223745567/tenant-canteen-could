package com.iotinall.canteen.dto.dish;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 口味
 *
 * @author loki
 * @date 2020/03/25 11:05
 */
@Data
@ApiModel(value = "口味")
public class SysFlavourDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "口味名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

}
