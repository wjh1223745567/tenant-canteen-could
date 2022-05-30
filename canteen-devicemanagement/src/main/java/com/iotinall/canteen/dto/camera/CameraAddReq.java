package com.iotinall.canteen.dto.camera;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加equ_face_device请求
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
@Data
@ApiModel(description = "添加equ_face_device请求")
public class CameraAddReq {
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "请填写名称")
    private String name;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    @ApiModelProperty(value = "安装位置", required = true)
    @NotBlank(message = "请输入安装位置")
    private String areaName;

    @ApiModelProperty(value = "安装位置", required = true)
    private Long tenantOrgId;

    //@NotBlank(message = "请上传默认图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String defaultImg;

    @NotBlank(message = "编号不能为空")
    private String deviceNo;
}