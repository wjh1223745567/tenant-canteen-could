package com.iotinall.canteen.dto.procurementplan;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InOrderProdReq {

    private Long id;

    private BigDecimal count;

    private BigDecimal price;

}
