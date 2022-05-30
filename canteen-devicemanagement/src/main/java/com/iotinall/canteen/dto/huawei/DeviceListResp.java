package com.iotinall.canteen.dto.huawei;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备列表
 *
 * @author loki
 * @date 2021/6/29 19:23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceListResp extends HuaweiApiResp {
    private CameraBriefInfoV2 cameraBriefInfosV2;
}
