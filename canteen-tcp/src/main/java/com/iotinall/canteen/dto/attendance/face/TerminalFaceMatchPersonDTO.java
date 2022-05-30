package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 人脸识别终端匹配人员信息
 *
 * @author loki
 * @date 2020/06/03 20:53
 */
@Data
public class TerminalFaceMatchPersonDTO {
    @JSONField(name = "PersonName")
    private String personName;

    @JSONField(name = "Gender")
    private Integer gender;

    @JSONField(name = "CardID")
    private String cardId;

    @JSONField(name = "IdentityNo")
    private String identityNo;

    @JSONField(name = "PersonCode")
    private String personCode;
}
