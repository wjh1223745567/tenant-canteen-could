package com.iotinall.canteen.dto.sensor;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 传感器
 *
 * @author loki
 * @date 2021/02/22 15:25
 */
@Data
public class SensorAddReq {
    private Long id;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空")
    private String deviceNo;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备用途 1-餐厅环境 2-后厨环境 3-仓库环境
     */
    @NotNull(message = "设备用途不能为空")
    private Integer type;

    /**
     * 备注
     */
    private String remark;
}
