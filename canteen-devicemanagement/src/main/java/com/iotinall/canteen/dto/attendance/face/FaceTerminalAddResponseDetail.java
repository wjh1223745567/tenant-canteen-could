package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 人脸终端请求返回体
 */
@Data
@Accessors(chain = true)
public class FaceTerminalAddResponseDetail implements Serializable {
    @JSONField(name = "ResponseURL", ordinal = 1)
    private String responseURL;

    @JSONField(name = "ResponseCode", ordinal = 2)
    private Integer responseCode;

    @JSONField(name = "ResponseString", ordinal = 3)
    private String responseString;

    @JSONField(name = "StatusCode", ordinal = 4)
    private Integer statusCode;

    @JSONField(name = "StatusString", ordinal = 5)
    private String statusString;

    @JSONField(name = "Data", ordinal = 6)
    private FaceTerminalAddPersonResultData data;
}
