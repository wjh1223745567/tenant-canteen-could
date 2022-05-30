package com.iotinall.canteen.common.protocol;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpDataSource {
    /**
     * 租户ID
     */
    private Long id;

    private String name;

    private String dataSourceKey;

}
