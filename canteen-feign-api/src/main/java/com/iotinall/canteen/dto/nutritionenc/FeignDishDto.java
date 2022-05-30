package com.iotinall.canteen.dto.nutritionenc;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class FeignDishDto implements Serializable {

    private String id;

    private String name;

    private String img;

}
