package com.iotinall.canteen.dto.lightkitchen;

import lombok.Data;

import java.io.Serializable;

/**
 * 明厨亮灶
 *
 * @author loki
 * @date 2021/02/23 20:07
 */
@Data
public class LightKitchenDTO implements Serializable {
    private String typeName;
    private String img;
    private String date;
}
