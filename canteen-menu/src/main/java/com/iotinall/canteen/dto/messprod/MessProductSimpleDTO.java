package com.iotinall.canteen.dto.messprod;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Data
@ApiModel(description = "菜品信息")
@Accessors(chain = true)
public class MessProductSimpleDTO implements Serializable {
    @ApiModelProperty(value = "菜品ID", required = true)
    private Long id;

    @ApiModelProperty(value = "菜品名称", required = true)
    private String name;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @ApiModelProperty(value = "图片")
    private String img;

    private Long energy;

    @ApiModelProperty(value = "评分")
    private Long score;

    @ApiModelProperty(value = "厨师姓名")
    private String cookName;

    @ApiModelProperty(value = "厨师头像")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String cookImg;
}