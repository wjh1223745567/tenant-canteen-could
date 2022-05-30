package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 人脸终端请求返回体
 */
@Data
public class FaceTerminalAddResultResponse implements Serializable {
    @JSONField(name = "Response")
    private FaceTerminalAddResponseDetail response;
}
