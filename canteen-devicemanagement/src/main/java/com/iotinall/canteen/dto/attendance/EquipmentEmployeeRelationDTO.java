package com.iotinall.canteen.dto.attendance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@ApiModel(value = "设备人员关系表返回对象", description = "设备人员关系表返回对象")
public class EquipmentEmployeeRelationDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "设备ID")
    private Long equId;

    @ApiModelProperty(value = "人员ID")
    private Long empId;

    @ApiModelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    @ApiModelProperty(value = "同步状态 0-未同步 1-已同步  2-无照片（未同步）")
    private Integer syncStatus;
}
