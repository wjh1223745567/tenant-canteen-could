package com.iotinall.canteen.dto.sso;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SsoAccessTokenResp {

    private String cosmosAccessToken;

    private LocalDateTime expiresTime;

    private String cosmosRefreshToken;

}
