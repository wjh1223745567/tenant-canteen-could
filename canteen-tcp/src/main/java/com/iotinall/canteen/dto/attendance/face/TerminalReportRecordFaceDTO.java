package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 人脸信息
 *
 * @author loki
 * @date 2020/06/02 17:48
 */
@Data
public class TerminalReportRecordFaceDTO {
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
     * FaceInfo 选择 1
     */
    @JSONField(name = "CapSrc")
    private Integer capSrc;

    /**
     * 半结构化特征数目如果没有半结构化特征，可不带相关字段
     */
    @JSONField(name = "FeatureNum")
    private Integer featureNum;

    /**
     * 半结构化特征列表，如果没有半结构化特征，可不带相关字段
     */
    @JSONField(name = "FeatureList")
    private String featureList;

    /**
     * 人脸半结构化特征提取算法，版本号，比如 "ISFRFR259.2.0"。范围：[0, 20]
     */
    @JSONField(name = "FeatureVersion")
    private String featureVersion;

    /**
     * 采用 base64 编码。基于人脸提取出来的特征信息，用于辅助后端服务器进行人脸比对等。目前加密前 512 Bytes。
     */
    @JSONField(name = "Feature")
    private String feature;

    /**
     * 体温 未知或未启用检测时，填 0
     */
    @JSONField(name = "Temperature")
    private Float temperature;

    /**
     * 是否戴口罩 0：未知或未启用检测 1：未戴口罩 2：戴口罩
     */
    @JSONField(name = "MaskFlag")
    private Integer maskFlag;

    /**
     * 人脸全景图，可根据需要选择上报,注：PTS 文件大小范围：[0,1M]
     */
    @JSONField(name = "PanoImage")
    private TerminalReportRecordFileDTO panoImage;

    /**
     * 人脸小图，可根据需要选择上报,文件大小范围：[0,256K]
     */
    @JSONField(name = "FaceImage")
    private TerminalReportRecordFileDTO faceImage;

    /**
     * 人脸全景图人脸区域坐标,在人脸大图中的人脸位置信息
     * 画面坐标归一化：0-10000 矩形左上和右下点：“138,315,282,684”
     */
    @JSONField(name = "FaceArea")
    private TerminalReportRecordFaceAreaDTO faceArea;
}
