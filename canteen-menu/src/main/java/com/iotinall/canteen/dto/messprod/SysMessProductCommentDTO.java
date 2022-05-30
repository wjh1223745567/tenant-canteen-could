package com.iotinall.canteen.dto.messprod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xin-bing
 * @date 10/29/2019 10:38
 */
@ApiModel
@Data
public class SysMessProductCommentDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论分数")
    private BigDecimal score;

    private LocalDateTime createTime;

    @ApiModelProperty(value = "点赞数")
    private Integer favorCount;

    @ApiModelProperty(value = "反对数")
    private Integer oppositeCount;

    @ApiModelProperty(value = "是否匿名 true - 是 ， false - 否")
    private Boolean anonymous;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty(value = "评论人id")
    private Long empId;

    @ApiModelProperty(value = "评论人昵称")
    private String empName;

    @ApiModelProperty(value = "评论者头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String empAvatar;
}
