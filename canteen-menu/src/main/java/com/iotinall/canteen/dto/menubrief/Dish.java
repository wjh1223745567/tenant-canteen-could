package com.iotinall.canteen.dto.menubrief;

import lombok.Data;

import java.util.List;

@Data
public class Dish {

    private String location;

    private List<DishProductDto> dishes;
}
