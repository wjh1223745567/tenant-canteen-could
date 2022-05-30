package com.iotinall.canteen.dto.auth;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class EditPwdReq {
    @NotNull(message = "请输入旧密码")
    @Pattern(regexp = "[A-Za-z0-9]{6,18}", message="密码由6-18位字母、数字组成")
    private String oldPwd;

    @NotNull(message = "请输入新密码")
    @Pattern(regexp = "[A-Za-z0-9]{6,18}", message="新密码由6-18位字母、数字组成")
    private String pwd;
}
