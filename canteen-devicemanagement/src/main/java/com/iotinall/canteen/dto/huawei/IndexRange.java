package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndexRange implements Serializable {
    private Integer fromIndex;

    private Integer toIndex;
}
