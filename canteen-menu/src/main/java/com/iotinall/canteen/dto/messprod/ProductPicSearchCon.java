package com.iotinall.canteen.dto.messprod;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ProductPicSearchCon {

    @NotNull
    private Integer menuType;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String base64;

}
