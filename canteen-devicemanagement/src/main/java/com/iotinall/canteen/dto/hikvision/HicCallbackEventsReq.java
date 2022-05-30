package com.iotinall.canteen.dto.hikvision;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class HicCallbackEventsReq {

    private String eventId;

    private Integer eventType;

    private LocalDateTime happenTime;

    private String srcIndex;

    private String srcName;

    private String srcParentIdex;

    private String srcType;

    private String status;

    private String eventLvl;

    private String timeout;

    /**
     * JSON 格式数据
     */
    private String data;

}
