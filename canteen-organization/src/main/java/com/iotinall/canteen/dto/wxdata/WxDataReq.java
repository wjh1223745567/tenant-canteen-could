package com.iotinall.canteen.dto.wxdata;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信需解密数据
 */
@Data
public class WxDataReq {

    @NotBlank
    private String encryptedData;

    @NotBlank
    private String iv;

    @NotBlank
    private String code;

}
