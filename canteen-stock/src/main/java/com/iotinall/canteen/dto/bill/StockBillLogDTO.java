package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author loki
 * @date 2020/09/05 17:17
 */
@ApiModel(description = "库存单据日志")
@Data
public class StockBillLogDTO implements Serializable {
    @ApiModelProperty(value = "当前正在执行的任务ID")
    private Integer currentTaskId;

    @ApiModelProperty(value = "对应任务")
    private List<StockBillLogDetailDTO> task;
}
