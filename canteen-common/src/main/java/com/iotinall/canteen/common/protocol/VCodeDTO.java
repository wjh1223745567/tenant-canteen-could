package com.iotinall.canteen.common.protocol;

import lombok.Getter;
import lombok.Setter;

/**
 * 验证码数据
 * @author xin-bing
 * @date 10/26/2019 11:59
 */
@Getter
@Setter
public class VCodeDTO {
    private String id;
    private String base64;
}
