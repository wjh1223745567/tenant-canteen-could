package com.iotinall.canteen.dto.morninginspect;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "晨检列表查询")
public class MorningInspectListReq {
    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "开始时间")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束时间")
    private LocalDate endDate;

    @ApiModelProperty(value = "姓名")
    private String name;
}
