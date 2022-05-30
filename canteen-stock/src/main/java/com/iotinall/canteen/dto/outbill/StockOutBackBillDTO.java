package com.iotinall.canteen.dto.outbill;

import com.iotinall.canteen.dto.bill.BaseStockBillDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 出/入库申请表
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Getter
@Setter
@ApiModel(description = "退货单据")
public class StockOutBackBillDTO extends BaseStockBillDTO {
    @ApiModelProperty(value = "明细")
    private List<StockOutBackBillDetailDTO> details;

    @ApiModelProperty(value = "对应出库单据号")
    private String outBillNo;
}
