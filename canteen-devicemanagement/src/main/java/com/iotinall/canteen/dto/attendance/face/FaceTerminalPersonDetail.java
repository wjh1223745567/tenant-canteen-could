package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author loki
 * @date 2020/06/11 20:35
 */
@Data
public class FaceTerminalPersonDetail implements Serializable {
    @JSONField(name = "PersonName", ordinal = 1)
    private String personName;

    @JSONField(name = "TimeTemplateList", ordinal = 2)
    private List<FaceTerminalPersonTimeTemplate> timeTemplateList;

    @JSONField(name = "IdentificationNum", ordinal = 3)
    private Integer identificationNum;

    @JSONField(name = "PersonID", ordinal = 4)
    private Long personId;

    @JSONField(name = "Remarks", ordinal = 5)
    private String remarks;

    @JSONField(name = "TimeTemplateNum", ordinal = 6)
    private Integer timeTemplateNum;

    @JSONField(name = "ImageNum", ordinal = 7)
    private Integer imageNum;

    @JSONField(name = "LastChange", ordinal = 8)
    private Long lastChange;

    @JSONField(name = "IdentificationList", ordinal = 9)
    private List<FaceTerminalPersonIdentification> identificationList;

    @JSONField(name = "PersonCode", ordinal = 10)
    private String personCode;

    @JSONField(name = "ImageList", ordinal = 11)
    private List<FaceTerminalPersonImage> imageList;
}
