package com.iotinall.canteen.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeignSimEmployeeDto {

    private Long id;
    private String name;
    private String img;
    private String idNo;

}
