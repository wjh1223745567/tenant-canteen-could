package com.iotinall.canteen.dto.recommend;

/**
 * @author WJH
 * @date 2019/11/610:06
 */

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@Accessors(chain = true)
public class RecommendDto {
    @ApiModelProperty(value = "评价ID", name = "id")
    private Long id;

    @ApiModelProperty(value = "头像", name = "image")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String image;

    @ApiModelProperty(value = "人名", name = "name")
    private String name;

    @ApiModelProperty(value = "星星数量", name = "star")
    private Double star;

    @ApiModelProperty(value = "标签", name = "tags")
    private String tags;

    @ApiModelProperty(value = "是否匿名", name = "anonymous")
    private Boolean anonymous;

    @ApiModelProperty(value = "评论", name = "info")
    private String info;

    @ApiModelProperty(value = "赞数量", name = "praiseCount")
    private Integer praiseCount;

    @ApiModelProperty(value = "是否已经赞", name = "isPraise")
    private Boolean isPraise;

    @ApiModelProperty(value = "踩数量", name = "treadCount")
    private Integer treadCount;

    @ApiModelProperty(value = "是否已经赞", name = "isTread")
    private Boolean isTread;

    @ApiModelProperty(value = "评论时间", name = "time")
    private String time;

    @ApiModelProperty(value = "是否是当前用户评论")
    private Boolean isUser;

    private Long createTime;
}
