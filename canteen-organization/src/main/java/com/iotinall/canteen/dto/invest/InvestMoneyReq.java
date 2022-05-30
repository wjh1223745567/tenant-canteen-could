package com.iotinall.canteen.dto.invest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author WJH
 * @date 2019/11/511:39
 */
@Setter
@Getter
@ApiModel(description = "充值参数")
public class InvestMoneyReq {

    @ApiModelProperty(value = "充值金额", name = "amount", required = true)
    @NotNull(message = "请输入充值金额")
    @DecimalMin(value = "0.01", message = "充值金额必须大于0.01元")
    private BigDecimal amount;
}
