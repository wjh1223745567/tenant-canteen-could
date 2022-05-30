package com.iotinall.canteen.dto.sensor.updata;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author loki
 * @date 2019-09-07 17:45
 **/
@Getter
@Setter
public class DeviceLocation implements Serializable {
    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 高度
     */
    private String altitude;
}
