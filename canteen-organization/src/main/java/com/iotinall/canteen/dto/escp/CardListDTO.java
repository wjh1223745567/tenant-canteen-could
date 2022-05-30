package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取卡列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class CardListDTO implements Serializable {
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
    @JsonProperty(value = "cardId")
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
     * 开卡时间
     */
    @JsonProperty(value = "commitdate")
    private String commitDate;
    /**
     * 卡状态：1--正常，3--挂失，4--冻结，5--灰色，6--停用，99--未发卡，8--限制存款
     */
    @JsonProperty(value = "accstatus")
    private String accStatus;

    /**
     * 失效期  yyyy-MM-dd
     */
    @JsonProperty(value = "effectdate")
    private String effectDate;

    /**
     * 机构编码
     */
    @JsonProperty(value = "organcode")
    private String organCode;

    /**
     * 版本号
     */
    private String ver;
    /**
     * 账户类型（关联卡账户绑定的卡类型）
     */
    @JsonProperty(value = "acctype")
    private String accType;

    /**
     * 账户类型名称,（关联卡账户绑定的卡类型名称）
     */
    @JsonProperty(value = "acctypename")
    private String accTypeName;
}
