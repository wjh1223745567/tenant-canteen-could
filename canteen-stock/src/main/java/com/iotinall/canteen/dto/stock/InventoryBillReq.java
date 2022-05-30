package com.iotinall.canteen.dto.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@ApiModel(value = "库存盘点请求参数")
@Data
public class InventoryBillReq implements Serializable {
    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "状态：1-待审核  2-待验收 3-已完成 4-已拒绝  5-已取消")
    private Integer status;

    @ApiModelProperty(value = "单据号")
    private String keyword;

}
