package com.iotinall.canteen.dto.equcamera;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author WJH
 * @date 2019/11/2811:21
 */
@Data
@Accessors(chain = true)
public class EquCameraDTO {

    private Long id;

    private String name;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String defaultImg;

    private Long deviceId;

    private Integer areaType;

    private String areaName;

    private Long tenantOrgId;

    private String tenantOrgName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String remark;
}
