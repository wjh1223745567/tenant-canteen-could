package com.iotinall.canteen.dto.outbill;

import com.iotinall.canteen.dto.bill.BaseStockDetailDTO;
import com.iotinall.canteen.dto.bill.StockBillDetailDTO;
import com.iotinall.canteen.dto.bill.StockDetailChangeRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 出入库明细（采购入库、领用出库、领用退库）
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Getter
@Setter
@ApiModel(description = "领用退库详情")
public class StockOutBackBillDetailDTO extends BaseStockDetailDTO {
    @ApiModelProperty(value = "出库单据")
    private StockBillDetailDTO outBill;

    @ApiModelProperty(value = "出库明细")
    private List<StockDetailChangeRecordDTO> outBillDetail;

    @ApiModelProperty(value = "退库明细")
    private List<StockDetailChangeRecordDTO> outBackBillDetail;
}
