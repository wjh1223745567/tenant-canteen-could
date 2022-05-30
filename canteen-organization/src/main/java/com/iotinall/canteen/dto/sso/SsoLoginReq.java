package com.iotinall.canteen.dto.sso;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SsoLoginReq {

    private String app_id;

    private String user_name;

    private String password;

}
