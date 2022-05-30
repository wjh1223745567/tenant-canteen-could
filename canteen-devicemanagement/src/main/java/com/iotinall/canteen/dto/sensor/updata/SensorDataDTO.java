package com.iotinall.canteen.dto.sensor.updata;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 温湿度传感器上报数据
 *
 * @author loki
 * @date 2020/10/10 15:28
 */
@Data
public class SensorDataDTO implements Serializable {
    private List<Att> att;
    private String deviceType;
    private Integer status;

    @Data
    public class Att {
        private String data;
        private String name;
        private String unit;
    }
}
