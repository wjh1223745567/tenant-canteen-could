package com.iotinall.canteen.dto.messdailymenu;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MessDailyMenuAddProductsReq {

    @NotNull
    private Long id;

    /**
     * 厨师ID
     */
    private Long cookId;

}
