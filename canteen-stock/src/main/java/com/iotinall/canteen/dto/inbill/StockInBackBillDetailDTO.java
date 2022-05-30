package com.iotinall.canteen.dto.inbill;

import com.iotinall.canteen.dto.bill.BaseStockDetailDTO;
import com.iotinall.canteen.dto.bill.StockBillDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 出入库明细（采购入库、领用出库、领用退库）
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Getter
@Setter
@ApiModel(description = "采购退货详情")
public class StockInBackBillDetailDTO extends BaseStockDetailDTO {
    @ApiModelProperty(value = "入库单据")
    private StockBillDetailDTO inBill;
}
