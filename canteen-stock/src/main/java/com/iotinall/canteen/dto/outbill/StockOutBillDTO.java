package com.iotinall.canteen.dto.outbill;

import com.iotinall.canteen.dto.bill.BaseStockBillDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 出库详情
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Getter
@Setter
@ApiModel(description = "出库明细")
public class StockOutBillDTO extends BaseStockBillDTO {

    @ApiModelProperty(value = "明细")
    private List<StockOutBillDetailDTO> details;
}
