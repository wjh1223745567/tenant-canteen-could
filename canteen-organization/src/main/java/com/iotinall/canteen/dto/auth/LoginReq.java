package com.iotinall.canteen.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求
 * @author xin-bing
 * @date 10/26/2019 13:54
 */
@Data
@ApiModel(description = "登录请求")
public class LoginReq {
    @ApiModelProperty(value = "登录名", required = true)
    @NotBlank(message = "请输入登录名")
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "请输入密码")
    private String pwd;
    @ApiModelProperty(value = "验证码id", required = true)
    private String vCodeId;
    @ApiModelProperty(value = "验证码", required = true)
    private String vCode;
}
