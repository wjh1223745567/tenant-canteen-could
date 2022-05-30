package com.iotinall.canteen.dto.notice;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 看板消息
 *
 * @author loki
 * @date 2021/02/23 20:07
 */
@Data
public class DashboardNoticeDTO implements Serializable {
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


    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @ApiModelProperty(value = "图片", name = "image")
    private String img;
}
