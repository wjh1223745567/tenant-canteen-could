package com.iotinall.canteen.dto.information;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 咨询列表
 * @author WJH
 * @date 2019/11/1116:29
 */
@Data
@Accessors(chain = true)
public class InformationDetailDTO {

    private Long id;

    @ApiModelProperty(value = "资讯类型", name = "type")
    private String type;

    @ApiModelProperty(value = "标题", name = "title")
    private String title;

    @ApiModelProperty(value = "内容", name = "content")
    private String content;

    @ApiModelProperty(value = "发布时间", name = "time")
    private String time;

    @ApiModelProperty(value = "是否置顶", name = "sticky")
    private Boolean sticky;

    @ApiModelProperty(value = "评论数量", name = "numberOfComments")
    private Integer numberOfComments;

    @ApiModelProperty(value = "赞数量", name = "praiseCount")
    private Integer praiseCount;

    @ApiModelProperty(value = "是否已赞", name = "isPraise")
    private Boolean isPraise;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "图片", name = "image")
    private String cover;

}
