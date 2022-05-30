package com.iotinall.canteen.dto.orgemployee;

import com.iotinall.canteen.common.jsr303.Pwd;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author bingo
 * @date 12/20/2019 20:35
 */
@Data
public class OrgEmpEditPwdReq {
    @NotNull(message = "请选择要修改的用户")
    private Long id;
    @Pwd
    private String pwd;
}
