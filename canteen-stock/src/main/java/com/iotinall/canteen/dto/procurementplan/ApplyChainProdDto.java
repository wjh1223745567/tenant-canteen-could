package com.iotinall.canteen.dto.procurementplan;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApplyChainProdDto {

    private Long id;

    private String name;

    private String certificate;

}
