package com.iotinall.canteen.dto.procurementplan;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ProcurementMaterialDto {

    private String name;

    /**
     * 用量
     */
    private BigDecimal dosage;

}
