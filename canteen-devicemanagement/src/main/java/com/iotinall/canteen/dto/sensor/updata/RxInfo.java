package com.iotinall.canteen.dto.sensor.updata;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author loki
 * @date 2019-09-07 17:53
 **/
@Getter
@Setter
public class RxInfo implements Serializable {
    private String gatewayID;
    private String name;
    private Integer rssi;
    private Integer loRaSNR;
    private DeviceLocation location;
}
