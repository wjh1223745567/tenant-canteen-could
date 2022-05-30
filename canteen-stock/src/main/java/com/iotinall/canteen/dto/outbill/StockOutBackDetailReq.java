package com.iotinall.canteen.dto.outbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 领用退库申请请求参数
 *
 * @author loki
 * @date 2020/05/04 16:45
 */
@Data
@ApiModel(description = "领用退库申请请求参数")
public class StockOutBackDetailReq implements Serializable {
    @ApiModelProperty(value = "出库明细ID")
    @NotNull(message = "出库明细ID不能为空")
    private Long outDetailId;

    @ApiModelProperty(value = "退库数量")
    @NotNull(message = "退库商品数量不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "退库当时对应的库存数量")
    private BigDecimal stockAmount;
}
