package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author loki
 * @date 2020/06/11 20:35
 */
@Data
public class FaceTerminalPersonTimeTemplate implements Serializable {
    @JSONField(name = "BeginTime", ordinal = 1)
    private Long beginTime;
    @JSONField(name = "EndTime", ordinal = 2)
    private Long endTime;
    @JSONField(name = "Index", ordinal = 3)
    private Long index;
}
