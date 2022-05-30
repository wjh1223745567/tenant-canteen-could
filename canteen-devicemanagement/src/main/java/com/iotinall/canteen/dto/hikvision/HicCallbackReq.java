package com.iotinall.canteen.dto.hikvision;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 海康事件回调参数
 */
@Data
@Accessors(chain = true)
public class HicCallbackReq {

    private String method;

    private HicCallbackParamsReq params;

}
