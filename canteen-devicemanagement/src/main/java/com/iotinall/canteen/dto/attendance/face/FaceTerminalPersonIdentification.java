package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 人脸终端身份信息
 *
 * @author loki
 * @date 2020/06/15 14:38
 */
@Data
public class FaceTerminalPersonIdentification implements Serializable {
    @JSONField(name = "Type")
    private Integer type;

    @JSONField(name = "Number")
    private String number;
}
