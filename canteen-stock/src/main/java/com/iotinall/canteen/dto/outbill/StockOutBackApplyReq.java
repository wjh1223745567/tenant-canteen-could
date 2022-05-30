package com.iotinall.canteen.dto.outbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 领用退库申请请求参数
 *
 * @author loki
 * @date 2020/05/03 11:21
 */
@Data
@ApiModel(description = "领用退库申请")
public class StockOutBackApplyReq implements Serializable {
    @ApiModelProperty("退库日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "单据号")
    @NotBlank(message = "单据号不能为空")
    private String billNo;

    @ApiModelProperty(value = "出库单据")
    @NotBlank(message = "出库单据不能为空")
    private String outBillNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "退库明细")
    @NotEmpty(message = "退库明细不能为空")
    private List<StockOutBackDetailReq> details;
}
