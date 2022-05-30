package com.iotinall.canteen.dto.camera;

import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
* @author xin-bing
* @date 2019-11-26 20:31:06
*/
@Data
@ApiModel(description = "查询equ_face_device条件")
public class CameraQueryCriteria {

    private String name;

    private Integer state;
}