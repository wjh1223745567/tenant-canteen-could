package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author loki
 * @date 2020/06/11 20:35
 */
@Data
public class FaceTerminalPersonImage implements Serializable {
    @JSONField(name = "FaceID", ordinal = 1)
    private Long faceId;

    @JSONField(name = "Name", ordinal = 2)
    private String name;

    @JSONField(name = "Size", ordinal = 3)
    private Integer size;

    @JSONField(name = "Data", ordinal = 4)
    private String data;
}
