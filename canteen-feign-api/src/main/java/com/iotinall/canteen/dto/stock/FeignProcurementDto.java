package com.iotinall.canteen.dto.stock;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class FeignProcurementDto {

    private List<MenuProd> breakfast = new ArrayList<>();

    private List<MenuProd> lunch = new ArrayList<>();

    private List<MenuProd> dinner = new ArrayList<>();

    /**
     * 菜品
     */
    @Getter
    @Setter
    public static class MenuProd {

        private Long id;

        private String name;

        private Long cookId;

        private String cookName;

        @JsonSerialize(using = ImgPair.ImgSerializer.class)
        private String cookImg;

        private String rawMaterial;
    }
}
