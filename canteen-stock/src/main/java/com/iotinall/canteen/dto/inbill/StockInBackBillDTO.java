package com.iotinall.canteen.dto.inbill;

import com.iotinall.canteen.dto.bill.BaseStockBillDTO;
import com.iotinall.canteen.dto.supplier.StockSupplierDTO;
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
public class StockInBackBillDTO extends BaseStockBillDTO {

    @ApiModelProperty(value = "供应商")
    private StockSupplierDTO supplier;

    @ApiModelProperty(value = "退货明细")
    private List<StockInBackBillDetailDTO> details;
}
