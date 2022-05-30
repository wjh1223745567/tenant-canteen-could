package com.iotinall.canteen.dto.tackout;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessProductTypeDto {

    private Long id;

    private String name;

}
