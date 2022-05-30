package com.iotinall.canteen.dto.sourcing;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OutSourcingCollectDto {

    private Long prodId;

    private String name;

    private String specification;

    private Integer count;

}
