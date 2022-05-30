package com.iotinall.canteen.dto.nutritionenc;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeignCraftDto {

    private String id;

    private String name;

}
