package com.iotinall.canteen.dto.warehouse;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 仓库类型
 **/
@Data
@Accessors(chain = true)
public class StockWarehouseTypeDTO {
    private Long id;

    private String name;

    private String remark;
}
