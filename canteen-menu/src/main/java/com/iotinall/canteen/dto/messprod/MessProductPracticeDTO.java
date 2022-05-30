package com.iotinall.canteen.dto.messprod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜品做法步骤明细s
 *
 * @author loki
 * @date 2020/03/26 11:54
 */
@Data
@ApiModel(description = "菜品做法步骤明细")
public class MessProductPracticeDTO implements Serializable {

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "文件类型 0- 图片 2-视频")
    private Integer fileType;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "文件路径")
    private String filePath;
}
