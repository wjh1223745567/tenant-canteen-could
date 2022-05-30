package com.iotinall.canteen.dto.attendance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "设备分页列表查询条件", description = "设备分页列表查询条件")
public class EquipmentQryReq implements Serializable {

    @ApiModelProperty(value = "关键字")
    private String keyword;

}
