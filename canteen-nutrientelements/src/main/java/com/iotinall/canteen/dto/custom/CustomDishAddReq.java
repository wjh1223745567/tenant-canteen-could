package com.iotinall.canteen.dto.custom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加自定义食物请求参数
 *
 * @author loki
 * @date 2020/04/13 14:08
 */
@ApiModel(value = "添加自定义食物请求参数")
@Data
public class CustomDishAddReq {
    @ApiModelProperty(value = "选择的系统食物ID")
    @NotBlank(message = "请选择食物")
    private String sysDishId;
}
