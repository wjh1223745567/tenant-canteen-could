package com.iotinall.canteen.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeignCookDto {

    private Long id;

    private String name;

}
