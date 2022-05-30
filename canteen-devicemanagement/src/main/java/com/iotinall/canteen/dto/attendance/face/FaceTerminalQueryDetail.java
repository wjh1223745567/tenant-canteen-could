package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 人脸识别终端查询超贱
 *
 * @author loki
 * @date 2020/06/11 16:09
 */
@Data
@Accessors(chain = true)
public class FaceTerminalQueryDetail implements Serializable {
    @JSONField(name = "QryType", ordinal = 1)
    private Long qryType;
    @JSONField(name = "QryCondition", ordinal = 2)
    private Long qryCondition;
    @JSONField(name = "QryData", ordinal = 3)
    private String qryData;
}
