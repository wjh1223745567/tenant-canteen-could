package com.iotinall.canteen.dto.bill;

import com.iotinall.canteen.constant.BillType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author WJH
 * @date 2019/11/514:16
 */
@Setter
@Getter
@Accessors(chain = true)
public class BillListDto {

    @ApiModelProperty(value = "标题 0 早餐 1午餐 2晚餐 3下午茶 4微信充值 5支付宝充值", name = "type")
    private BillType type;

    @ApiModelProperty(value = "金额", name = "amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "时间", name = "time")
    private String time;

}
