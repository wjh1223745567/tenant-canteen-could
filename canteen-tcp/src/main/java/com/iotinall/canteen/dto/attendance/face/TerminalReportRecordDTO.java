package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 人脸识别终端设备上报信息
 *
 * @author loki
 * @date 2020/06/02 16:42
 */
@Data
public class TerminalReportRecordDTO {
    /**
     * 订阅者描述信息，以 URL格式体现。
     */
    @JSONField(name = "Reference")
    private String reference;

    /**
     * 通知记录序号
     */
    @JSONField(name = "Seq")
    private Long seq;

    /**
     * 设备编码(设备序列号) 范围:[0 24]
     */
    @JSONField(name = "DeviceCode")
    private String deviceCode;

    /**
     * 记录日期
     */
    @JSONField(name = "Timestamp")
    private Long timestamp;

    /**
     * 通知类型 0：实时通知 1：历史通知
     */
    @JSONField(name = "NotificationType")
    private Integer notificationType;

    /**
     * 人脸信息数目，范围：[0, 1] 当采集信息没有人脸时，可不带 FaceInfo 相关字段
     */
    @JSONField(name = "FaceInfoNum")
    private Integer faceInfoNum;

    /**
     * 卡信息数目 范围：[0, 1] 当采集信息没有卡证时，可不带 CardInfo 相关字段
     */
    @JSONField(name = "CardInfoNum")
    private Integer cardInfoNum;

    /**
     * 闸机信息数目 范围：[0, 1]
     * 当采集信息没有闸机信息时，可不带 GateInfo 相关字段
     */
    @JSONField(name = "GateInfoNum")
    private Integer gateInfoNum;

    /**
     * 库比对信息数目 范围：[0, 16] 当采集类型为人脸采集时， 可不带 LibMatInfo 相关字段
     */
    @JSONField(name = "LibMatInfoNum")
    private Integer libMatInfoNum;

    /**
     * 人脸信息列表，详见<FaceInfoList>;
     * 当采集信息没有人脸时，可不带 FaceInfo 相关字段
     */
    @JSONField(name = "FaceInfoList")
    private List<TerminalReportRecordFaceDTO> faceInfoList;

    /**
     * 卡信息列表, 详见 <CardInfoList>;
     * 当采集信息没有卡证时，可不带 CardInfo 相关字段
     */
    @JSONField(name = "CardInfoList")
    private List<TerminalReportRecordCardDTO> cardInfoList;

    /**
     * 闸机信息列表, 详见<GateInfoList>;
     * 当采集信息没有闸机信息时，可不带 GateInfo 相关字段
     */
    @JSONField(name = "GateInfoList")
    private List<TerminalReportRecordGateDTO> gateInfoList;

    /**
     * 库比对信息列表，详见<CtrlLibMatInfo>;
     * 当采集类型为人脸采集时，可不带 LibMatInfo 相关字段
     */
    @JSONField(name = "LibMatInfoList")
    private List<TerminalReportRecordLibMatDTO> libMatInfoList;
}
