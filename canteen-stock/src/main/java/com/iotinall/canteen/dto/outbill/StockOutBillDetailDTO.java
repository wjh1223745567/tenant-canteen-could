package com.iotinall.canteen.dto.outbill;

import com.iotinall.canteen.dto.bill.BaseStockDetailDTO;
import com.iotinall.canteen.dto.bill.StockDetailChangeRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 出库单据明细
 *
 * @author loki
 * @date 2020/09/04 12:00
 */
@Getter
@Setter
@ApiModel(description = "出库明细")
public class StockOutBillDetailDTO extends BaseStockDetailDTO {

    @ApiModelProperty(value = "出库明细")
    List<StockDetailChangeRecordDTO> outDetails;
}
