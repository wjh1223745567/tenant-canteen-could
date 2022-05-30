package com.iotinall.canteen.dto.dish;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DishListDTO {

    private String id;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img;

    private String name;

    /**
     * 菜品工艺
     */
    private String craftName;

    /**
     * 菜品口味
     */
    private String flavourName;

}
