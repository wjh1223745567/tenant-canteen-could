package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 分页参数
 */
@Data
public class FaceTerminalPageResult {
    @JSONField(name = "Limit", ordinal = 3)
    private Integer limit;

    @JSONField(name = "Offset", ordinal = 4)
    private Integer offset;
}
