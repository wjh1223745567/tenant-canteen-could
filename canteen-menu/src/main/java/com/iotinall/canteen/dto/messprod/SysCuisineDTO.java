package com.iotinall.canteen.dto.messprod;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜品类型
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@Data
@ApiModel(description = "菜品类型")
public class SysCuisineDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "类别名称")
    private String name;
}
