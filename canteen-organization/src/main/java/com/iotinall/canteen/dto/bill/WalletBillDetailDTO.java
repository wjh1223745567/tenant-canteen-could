package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单明细
 *
 * @author loki
 * @date 2021/01/14 15:16
 */
@Data
@ApiModel(value = "账单")
public class WalletBillDetailDTO {
    private Long id;

    @ApiModelProperty(value = "金额变动值")
    private BigDecimal amount;

    @ApiModelProperty(value = "0-减少 1-添加")
    private Integer type;

    @ApiModelProperty(value = "账单变动说明 1-早餐消费 2-午餐消费 3-晚餐消费 4-打款充值 5-现金充值 6-微信充值 7-支付宝充值 8-其他充值")
    private String reason;

    @ApiModelProperty(value = "变动日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "记录类型：0-充值 1-消费")
    private Integer recordType;
}
