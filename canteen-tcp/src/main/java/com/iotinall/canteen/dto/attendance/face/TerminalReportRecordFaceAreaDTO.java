package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author loki
 * @date 2020/06/03 11:23
 */
@Data
public class TerminalReportRecordFaceAreaDTO {
    /**
     * 左上角 x 坐标。
     */
    @JSONField(name = "LeftTopX")
    private Long leftTopX;

    /**
     * 左上角 y 坐标。
     */
    @JSONField(name = "LeftTopY")
    private Long leftTopY;

    /**
     * 右下角 x 坐标。
     */
    @JSONField(name = "RightBottomX")
    private Long rightBottomX;

    /**
     * 右下角 y 坐标。
     */
    @JSONField(name = "RightBottomY")
    private Long rightBottomY;
}
