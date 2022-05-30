package com.iotinall.canteen.dto.messprod;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessProductSportDto {
    private Long id;
    private String name;
    private Long energy;
    private Long protein;
    private Long fat;
    private Long carbohydrate;
    private String img;
}
