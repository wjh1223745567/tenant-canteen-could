package com.iotinall.canteen.dto.sensor.updata;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 温湿度传感器上报数据
 *
 * @author loki
 * @date 2020/10/10 15:28
 */
@Data
@AllArgsConstructor
public class TempHumDTO implements Serializable {

    /**
     * 温度
     */
    private Float temp;
    /**
     * 湿度
     */
    private Float hum;
    private Float pm25;
    private Float pm10;
    private Float co2;
    /**
     * 甲醛
     */
    private Float hcho;

    /**
     * 噪声
     */
    private Float noise;

    /**
     * 0 表示告警
     */
    private Integer alarm;

    /**
     * 设备类型
     */
    private Integer deviceType;

    private Float voltage;
    private String err;

    private String devEUI;
}
