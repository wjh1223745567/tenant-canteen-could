package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取人员列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class EmployeeListDTO {
    /**
     * 人员信息id
     */
    @JsonProperty(value = "userId")
    private String userid;

    /**
     * 工号
     */
    @JsonProperty(value = "idserial")
    private String idSerial;

    /**
     * 姓名
     */
    private String username;

    /**
     * 0 女 1男
     */
    @JsonProperty(value = "sex")
    private Integer sex;

    /**
     * 部门编码
     */
    @JsonProperty(value = "departid")
    private Integer departId;

    /**
     * 是否发卡 0未发卡 1已发卡
     */
    @JsonProperty(value = "iscard")
    private Integer isCard;

    /**
     * 入职时间
     */
    @JsonProperty(value = "inschooldate")
    private String inSchoolDate;

    /**
     * 1 在职人员 2离职人员 3退休人员 4离休人员 5外部人员
     */
    @JsonProperty(value = "zipcode")
    private Integer zipCode;

    /**
     * 年月日时分秒
     */
    @JsonProperty(value = "commitdate")
    private String commitDate;

    /**
     * 国家
     */
    @JsonProperty(value = "countrycode")
    private String countryCode;

    /**
     * 民族
     */
    @JsonProperty(value = "nationcode")
    private String nationCode;

    /**
     * 身份代码
     */
    @JsonProperty(value = "pcode")
    private String pCode;

    /**
     * 证件类型
     */
    @JsonProperty(value = "idtype2")
    private String idType2;

    /**
     * 第二证件号
     */
    @JsonProperty(value = "idserial2")
    private String idSerial2;

    /**
     * 电话
     */
    private String tel;

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
