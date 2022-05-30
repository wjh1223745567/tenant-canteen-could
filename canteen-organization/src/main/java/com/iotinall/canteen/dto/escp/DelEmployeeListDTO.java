package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取注销人员列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class DelEmployeeListDTO {
    /**
     * 人员信息id
     */
    @JsonProperty(value = "userid")
    private Long userId;

    /**
     * 工号
     */
    @JsonProperty(value = "idserial")
    private String idSerial;

    /**
     * 姓名
     */
    @JsonProperty(value = "username")
    private String username;

    /**
     * 身份证号
     */
    @JsonProperty(value = "idserial2")
    private String idSerial2;

    /**
     * 卡物理号
     */
    @JsonProperty(value = "cardid")
    private String cardId;

    /**
     * 卡唯一号
     */
    @JsonProperty(value = "scardsnr")
    private String sCardSnr;

    /**
     * 卡号
     */
    @JsonProperty(value = "cardno")
    private String cardNo;

    /**
     * 注销时间  yyyy-MM-dd hh:mm:ss
     */
    @JsonProperty(value = "deluserdate")
    private String delUserDate;

    /**
     * 机构编码
     */
    @JsonProperty(value = "organcode")
    private Integer organCode;

    /**
     * 版本号
     */
    private Long ver;
}
