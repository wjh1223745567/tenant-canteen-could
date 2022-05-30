package com.iotinall.canteen.dto.sensor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 传感器
 *
 * @author loki
 * @date 2021/02/22 15:25
 */
@Data
@ApiModel(value = "传感器返回参数")
public class SensorDTO {
    @ApiModelProperty(value = "设备ID")
    private Long id;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String name;

    /**
     * 设备设备用途 1-餐厅环境 2-后厨环境 3-仓库环境
     */
    @ApiModelProperty(value = "设备用途 1-餐厅环境 2-后厨环境 3-仓库环境")
    private Integer type;

    /**
     * 状态 0-待激活 1-正常 2-异常
     */
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 实时数据
     */
    @ApiModelProperty(value = "实时数据")
    private String data;
}
