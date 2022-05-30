package com.iotinall.canteen.dto.suppliertype;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StockSupplierTypeDTO {
    private Long id;
    private String name;
    private String remark;
}
