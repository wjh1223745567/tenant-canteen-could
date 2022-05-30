package com.iotinall.canteen.dto.notice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DashboardNoticeEdit {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", name = "title")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", name = "content")
    private String content;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "图片", name = "image")
    private String img;
}
