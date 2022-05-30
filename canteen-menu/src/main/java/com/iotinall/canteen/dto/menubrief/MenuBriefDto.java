package com.iotinall.canteen.dto.menubrief;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MenuBriefDto {

    private Long id;

    private String name;

    private String mealType;

}
