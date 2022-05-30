package com.iotinall.canteen.dto.sms;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 短信验证码登入请求参数
 *
 * @author loki
 * @date 2020/04/29 15:05
 */
@Data
public class MobileLoginReq implements Serializable {
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "openId不能为空")
    private String openId;
}
