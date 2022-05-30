package com.iotinall.canteen.dto.inventory;


import com.iotinall.canteen.dto.bill.BaseStockBillDTO;
import com.iotinall.canteen.dto.warehouse.StockWarehouseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * 盘点详情列表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ApiModel(description = "返回盘点详情结果")
public class StockInventoryBillDTO extends BaseStockBillDTO {

    @ApiModelProperty(value = "仓库")
    private StockWarehouseDTO warehouse;

    @ApiModelProperty(value = "盘点库存明细")
    private List<StockInventoryBillDetailDTO> billDetails;
}
