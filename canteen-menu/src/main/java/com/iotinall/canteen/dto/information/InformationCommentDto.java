package com.iotinall.canteen.dto.information;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author WJH
 * @date 2019/11/1518:08
 */
@Data
@Accessors(chain = true)
public class InformationCommentDto {

    private Long id;

    @ApiModelProperty(value = "评论人", name = "name")
    private String name;

    @ApiModelProperty(value = "评论内容", name = "content")
    private String content;

    @ApiModelProperty(value = "头像", name = "headImage")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String headImage;

    @ApiModelProperty(value = "当前用户是否已赞", name = "content")
    private Boolean isPraise;

    @ApiModelProperty(value = "赞数量", name = "praise")
    private Integer praise;

    @ApiModelProperty(value = "是否匿名", name = "anonymous")
    private Boolean anonymous;

    private Boolean isUser;

    @ApiModelProperty(value = "创建时间毫秒值")
    private Long createTime;
}
