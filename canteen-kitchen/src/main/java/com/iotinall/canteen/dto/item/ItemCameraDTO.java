package com.iotinall.canteen.dto.item;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "区域摄像头")
public class ItemCameraDTO {

    @ApiModelProperty(value = "摄像头ID")
    private Long cameraId;

    @ApiModelProperty(value = "摄像头名称")
    private String cameraName;

}
