package com.iotinall.canteen.dto.sso;

import lombok.Data;

@Data
public class SsoUserInfoBaseResp {

    /**
     * 工号
     */
    private String account;

    private String email;

    private String mobile;

    private String name;

}
