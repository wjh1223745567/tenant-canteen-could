package com.iotinall.canteen.dto.warehouse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 仓库
 **/
@Data
@ApiModel(value = "仓库对象")
public class StockWarehouseSimpleDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "仓库全称")
    private String fullName;
}
