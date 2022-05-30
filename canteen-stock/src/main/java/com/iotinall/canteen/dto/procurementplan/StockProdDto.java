package com.iotinall.canteen.dto.procurementplan;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class StockProdDto {

    private Long id;

    /**
     * 页面传入的名称，非数据库名称
     */
    private String name;

    private String trueName;

    private String code;

    private String typeName;

    private BigDecimal price;

    private String specs;

    private Double vectorValue;
}
