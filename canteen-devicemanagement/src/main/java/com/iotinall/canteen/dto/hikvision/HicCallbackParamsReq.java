package com.iotinall.canteen.dto.hikvision;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class HicCallbackParamsReq {

    private LocalDateTime sendTime;

    private String ability;

    private List<HicCallbackEventsReq> events;

}
