package com.iotinall.canteen.dto.invest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付返回实体
 * @author WJH
 * @date 2019/11/711:32
 */
@Data
@Accessors(chain = true)
public class InvestMoneyDto {

    @ApiModelProperty(value = "微信返回需要请求的参数Object", name = "data")
    private Object data;
}
