package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author loki
 * @date 2020/06/11 20:33
 */
@Data
public class FacePageResult {
    @JSONField(name = "Total", ordinal = 3)
    private Integer total;

    @JSONField(name = "Offset", ordinal = 4)
    private Integer offset;
}
