package com.iotinall.canteen.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VectorComputDto {

    private String dataId;

    private Double[] vector;

}
