package com.iotinall.canteen.dto.suppliertype;

import lombok.Data;

/**
 * 供应商类型请求参数
 */
@Data
public class StockSupplierV2TypeQuerReq {
    private Long id;
    private Long amount;
    private Long stockAmount;
}
