package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraBriefInfoV2 implements Serializable {
    private Integer total;

    private IndexRange indexRange;

    private CameraBriefInfoList cameraBriefInfoList;
}
