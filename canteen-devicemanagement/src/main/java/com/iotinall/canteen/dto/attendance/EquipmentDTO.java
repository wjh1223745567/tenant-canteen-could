package com.iotinall.canteen.dto.attendance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@ApiModel(value = "终端返回对象", description = "终端返回对象")
public class EquipmentDTO {
    @ApiModelProperty(value = "设备主键ID")
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
     * 设备IP
     */
    @ApiModelProperty(value = "设备IP")
    private String ip;

    /**
     * 设备类型 0-闸机 1-人脸识别终端  2-读卡器 3-发卡器
     */
    private Integer type;

    /**
     * 状态 0-待激活 1-正常 2-异常
     */
    @ApiModelProperty(value = "状态 0-待激活 1-正常 2-异常")
    private Integer status;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 设备人脸库
     */
    @ApiModelProperty(value = "设备人脸库")
    private Long empLib;

    /**
     * 安装位置
     */
    private Long tenantOrgId;

    /**
     * 详细位置
     */
    private String areaName;
}
