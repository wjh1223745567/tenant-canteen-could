package com.iotinall.canteen.dto.messdailymenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加厨师请求参数
 *
 * @author loki
 * @date 2021/01/23 13:52
 */
@Data
@ApiModel(value = "添加厨师请求参数")
public class MessDailyMenuCookAddReq implements Serializable {
    @ApiModelProperty(value = "排菜ID")
    @NotNull(message = "排菜ID不能为空")
    private Long itemId;

    @ApiModelProperty(value = "厨师ID")
    @NotNull(message = "厨师ID不能为空")
    private Long cookId;
}
