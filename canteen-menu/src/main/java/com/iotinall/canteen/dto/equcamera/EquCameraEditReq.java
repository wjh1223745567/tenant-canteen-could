package com.iotinall.canteen.dto.equcamera;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加餐区分布图
 * @author WJH
 * @date 2019/11/2811:21
 */
@Data
public class EquCameraEditReq {

    @NotNull
    private Long id;

    @NotBlank(message = "名称不能为空")
    @Length(max = 20, message = "名称长度不能超过20个字符")
    private String name;


    @NotNull(message = "请选择设备")
    private Long deviceId;

    //@NotBlank(message = "请上传默认图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String defaultImg;

    @NotNull(message = "请选择区域类型")
    private Integer areaType;

    private String remark;
}
