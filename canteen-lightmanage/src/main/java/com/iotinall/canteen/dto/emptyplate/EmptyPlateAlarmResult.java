package com.iotinall.canteen.dto.emptyplate;

import lombok.Data;

import java.io.Serializable;

/**
 * 光盘行动报警请求参数
 *
 * @author loki
 * @date 2021/04/14 11:39
 */
@Data
public class EmptyPlateAlarmResult implements Serializable {
    private Integer label_id;
    private String label_name;
    private Double score;
    private Double x0;
    private Double x1;
    private Double y0;
    private Double y1;
}
