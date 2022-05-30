package com.iotinall.canteen.dto.messprod;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class FeignMessProdDto implements Serializable {

    private Long id;

    private String name;

    private String img;

    private Long energy;
}
