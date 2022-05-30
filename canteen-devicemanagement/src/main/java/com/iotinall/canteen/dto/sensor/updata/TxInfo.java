package com.iotinall.canteen.dto.sensor.updata;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author loki
 * @date 2019-09-07 17:54
 **/
@Getter
@Setter
public class TxInfo implements Serializable {
    private Integer frequency;
    private Integer dr;
}
