package com.iotinall.canteen.dto.inbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 采购退货请求参数
 *
 * @author loki
 * @date 2020/05/03 11:21
 */
@Data
@ApiModel(description = "采购退货请求参数")
public class StockInBackApplyReq implements Serializable {

    @ApiModelProperty(value = "单据号")
    @NotBlank(message = "单据号不能为空")
    private String billNo;

    @ApiModelProperty(value = "退货日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "供应商ID")
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    @ApiModelProperty(value = "退货明细")
    @NotEmpty(message = "退货明细不能为空")
    private List<StockInBackDetailApplyReq> details;

    @ApiModelProperty(value = "备注")
    private String remark;
}
