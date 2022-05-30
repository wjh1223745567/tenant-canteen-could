package com.iotinall.canteen.dto.devicemanagement;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeignCameraDto {

    private Long id;

    private String name;

    private String image;

    private String deviceNo;

    private Long tenantOrgId;

    private String areaName;
}
