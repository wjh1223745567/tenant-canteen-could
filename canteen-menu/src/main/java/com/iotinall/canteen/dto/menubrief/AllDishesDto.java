package com.iotinall.canteen.dto.menubrief;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AllDishesDto {

    private String name;

    private BigDecimal probability;

}
