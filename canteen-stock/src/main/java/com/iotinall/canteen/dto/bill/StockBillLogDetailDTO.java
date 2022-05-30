package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author loki
 * @date 2020/09/05 17:17
 */
@ApiModel(description = "库存单据日志")
@Data
public class StockBillLogDetailDTO implements Serializable {
    @ApiModelProperty(value = "流程名称")
    private String taskDefine;

    @ApiModelProperty(value = "任务ID")
    private Integer taskId;

    @ApiModelProperty(value = "对应操作日志")
    private StockBillOperateLogDTO log;
}
