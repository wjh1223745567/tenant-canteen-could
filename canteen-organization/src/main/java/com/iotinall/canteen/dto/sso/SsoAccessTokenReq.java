package com.iotinall.canteen.dto.sso;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SsoAccessTokenReq {

    private String app_id;

    private String authorization_code;

    private String grant_type = "authorization_code";

}
