package com.iotinall.canteen.dto.huawei;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 登入接口请求参数
 *
 * @author loki
 * @date 2021/6/28 20:27
 **/
@Data
@Accessors(chain = true)
public class LoginReq implements Serializable {
    private String userName;
    private String password;
}
