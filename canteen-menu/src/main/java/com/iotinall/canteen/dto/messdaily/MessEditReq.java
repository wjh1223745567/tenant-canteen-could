package com.iotinall.canteen.dto.messdaily;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加餐厅请求
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Data
@ApiModel(description = "修改餐厅请求")
public class MessEditReq {

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "餐厅名称")
    private String name;

    @ApiModelProperty(value = "餐位数量")
    @Min(value = 1, message = "餐位数量不能小于1")
    private Integer capacity;
}