package com.iotinall.canteen.utils.sms;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 短信内容
 *
 * @author loki
 * @date 2020/04/29 11:30
 */
@Data
@Accessors(chain = true)
public class SmsForm {

    private String[] captcha;

    private String mobile;

    private String[] mobiles;
}
