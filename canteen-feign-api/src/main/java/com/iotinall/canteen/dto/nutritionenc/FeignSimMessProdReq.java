package com.iotinall.canteen.dto.nutritionenc;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class FeignSimMessProdReq {

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotBlank(message = "疾病不能为空")
    private String personDisease;

}
