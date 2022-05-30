package com.iotinall.canteen.dto.picturevector;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class FeignVectorReq {

    /**
     * ID
     */
    @NotBlank
    private String group;

    /**
     * 数据ID,已存在数时覆盖
     */
    @NotBlank
    private String dataId;

    /**
     * 图片base64
     */
    @NotBlank
    private String base64;
}
