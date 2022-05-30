package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CameraBriefInfoList implements Serializable {
    private List<CameraBriefInfo> cameraBriefInfo;
}
