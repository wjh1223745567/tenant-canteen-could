package com.iotinall.canteen.dto.sensor.updata;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 报警器消息
 *
 * @author loki
 * @date 2019-09-07 15:05
 **/
@Getter
@Setter
public class DevicePayload implements Serializable {
    private String applicationID;
    private String applicationName;
    private String deviceName;
    private String devEUI;
    private Boolean adr;
    private Integer fCnt;
    private Integer fPort;
    private String data;
    private List<RxInfo> rxInfo;
    private TxInfo txInfo;
    private String objectJSON;
}
