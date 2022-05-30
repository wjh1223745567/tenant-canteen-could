package com.iotinall.canteen.dto.warehouse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 仓库管理员DTO
 *
 * @author loki
 * @date 2020/08/28 17:22
 */
@Data
@ApiModel(value = "仓库管理员DTO")
public class StockWarehouseManagerDTO implements Serializable {
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String mobile;
}
