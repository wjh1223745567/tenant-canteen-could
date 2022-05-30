package com.iotinall.canteen.dto.emptyplate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 光盘行动报警请求参数
 *
 * @author loki
 * @date 2021/04/14 11:39
 */
@Data
public class EmptyPlateAlarmReq implements Serializable {
    private String base64_img;
    private String device_id;
    private String device_name;
    private String device_sn;
    private Integer model_id;
    private List<EmptyPlateAlarmResult> result;
}
