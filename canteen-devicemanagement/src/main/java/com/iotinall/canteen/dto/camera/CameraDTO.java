package com.iotinall.canteen.dto.camera;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "摄像头结果")
public class CameraDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "name", required = true)
    private String name;

    private String deviceNo;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img;

    @ApiModelProperty(value = "create_time", required = true)
    private LocalDateTime createTime;// create_time

    @ApiModelProperty(value = "update_time", required = true)
    private LocalDateTime updateTime;// update_time

    @ApiModelProperty(value = "remark", required = true)
    private String remark;// remark

    @ApiModelProperty(value = "详细位置", required = true)
    private String areaName;

    @ApiModelProperty(value = "安装位置", required = true)
    private Long tenantOrgId;

    @ApiModelProperty(value = "默认图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String defaultImg;
}