package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 卡信息列表
 *
 * @author loki
 * @date 2020/06/02 17:48
 */
@Data
public class TerminalReportRecordCardDTO {
    /**
     * 记录 ID
     */
    @JSONField(name = "ID")
    private Long id;
    /**
     * 采集时间
     */
    @JSONField(name = "Timestamp")
    private Long Timestamp;
    /**
     * 采集来源
     * 1：人脸识别终端采集的人脸信息;
     * 2：读卡器采集的门禁卡信息;
     * 3：读卡器采集的身份证信息;
     * 4：闸机采集的闸机信息;
     * CardInfo 选择 2 或 3
     */
    @JSONField(name = "CapSrc")
    private Integer capSrc;

    /**
     * 0：身份证，1：门禁卡
     */
    @JSONField(name = "CardType")
    private Integer cardType;

    /**
     * 门禁卡字段：物理卡号，最长18 位
     */
    @JSONField(name = "CardID")
    private String cardId;

    /**
     * 门禁卡字段：卡状态：1：有效，0：无效
     */
    @JSONField(name = "CardStatus")
    private Integer cardStatus;

    /**
     * 身份证字段：姓名，范围 [1,63]
     */
    @JSONField(name = "Name")
    private String name;

    /**
     * 身份证字段：0-未知的性别 1-男 2-女 9-未说明的性别
     */
    @JSONField(name = "Gender")
    private Integer gender;

    /**
     * 身份证字段：民族，参考GB/T 3304 中国各民族名称的罗马字母拼写法和代码01：汉族…
     */
    @JSONField(name = "Ethnicity")
    private Integer ethnicity;

    /**
     * 身份证字段：出生日期，格式为 YYYYMMDD
     */
    @JSONField(name = "Birthday")
    private String birthday;

    /**
     * 身份证字段：住址
     */
    @JSONField(name = "ResidentialAddress")
    private String residentialAddress;

    /**
     * 身份证字段：身份证号码，最长 18 位
     */
    @JSONField(name = "IdentityNo")
    private String identityNo;

    /**
     * 身份证字段：发证机关，一般格式为：XX 省 XX 市XX 区（县）公安分局
     */
    @JSONField(name = "IssuingAuthority")
    private String issuingAuthority;

    /**
     * 身份证字段：发证日期，格式为 YYYYMMDD
     */
    @JSONField(name = "IssuingDate")
    private String issuingDate;

    /**
     * 身份证字段：证件有效期开始时间，格式为YYYYMMDD
     */
    @JSONField(name = "ValidDateStart")
    private String validDateStart;

    /**
     * 身份证字段：证件有效期结束时间，格式为YYYYMMDD
     */
    @JSONField(name = "ValidDateEnd")
    private String validDateEnd;

    /**
     * 身份证字段：身份证照片 注：PTS 文件大小范围：[0,32K]
     */
    @JSONField(name = "IDImage")
    private TerminalReportRecordFileDTO idImage;
}
