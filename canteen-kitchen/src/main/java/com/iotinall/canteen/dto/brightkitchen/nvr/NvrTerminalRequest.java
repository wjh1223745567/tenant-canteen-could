package com.iotinall.canteen.dto.brightkitchen.nvr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * nvr 上报消息请求
 */
@Data
public class NvrTerminalRequest implements Serializable {
    @JSONField(name = "Reference")
    private String reference;

    @JSONField(name = "AlarmType")
    private String alarmType;

    @JSONField(name = "TimeStamp")
    private String timeStamp;

    @JSONField(name = "SourceName")
    private String sourceName;

    @JSONField(name = "RelatedID")
    private String relatedID;

    @JSONField(name = "AlarmPicture")
    private NvrAlarmPicture alarmPicture;
}
