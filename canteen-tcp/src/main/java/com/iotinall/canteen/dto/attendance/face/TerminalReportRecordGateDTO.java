package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 闸机信息列表
 *
 * @author loki
 * @date 2020/06/02 17:48
 */
@Data
public class TerminalReportRecordGateDTO {
    /**
     * 记录ID
     */
    @JSONField(name = "ID")
    private Long id;

    /**
     * 采集时间
     */
    @JSONField(name = "Timestamp")
    private Long timestamp;
    /**
     * 采集来源
     * 1：人脸识别终端采集的人脸信息;
     * 2：读卡器采集的门禁卡信息;
     * 3：读卡器采集的身份证信息;
     * 4：闸机采集的闸机信息;
     * GateInfo 选择 4
     */
    @JSONField(name = "CapSrc")
    private Integer capSrc;

    /**
     * 进入人员计数
     */
    @JSONField(name = "InPersonCnt")
    private Long inPersonCnt;

    /**
     * 出人员计数
     */
    @JSONField(name = "OutPersonCnt")
    private Long outPersonCnt;
}
