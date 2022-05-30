package com.iotinall.canteen.dto.stock;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class FeignStockProdDto {

    private Long id;

    private String name;

    /**
     * 库存质量
     */
    private BigDecimal quality;

}
