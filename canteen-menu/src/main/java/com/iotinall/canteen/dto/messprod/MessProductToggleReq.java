package com.iotinall.canteen.dto.messprod;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xin-bing
 * @date 10/30/2019 09:36
 */
@ApiModel(description = "上下架请求")
@Data
public class MessProductToggleReq {
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "商品id不能为空")
    private Long id;
    @ApiModelProperty(value = "上下架", required = true)
    @NotNull(message = "上下架状态不正确")
    private Boolean enabled;
}
