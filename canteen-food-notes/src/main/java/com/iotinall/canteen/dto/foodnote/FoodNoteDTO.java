package com.iotinall.canteen.dto.foodnote;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 美食笔记DTO
 * @author: JoeLau
 * @time: 2021年07月06日 14:34:44
 */
@Data
@ApiModel(value = "返回美食笔记DTO")
public class FoodNoteDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 标题
     */
    @ApiModelProperty(value = "美食笔记标题")
    private String title;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 作者姓名
     */
    @ApiModelProperty(value = "笔记作者姓名")
    private String authorName;

    /**
     * 作者头像
     */
    @ApiModelProperty(value = "笔记作者头像")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String authorAvatar;

    /**
     * 笔记类型名
     */
    @ApiModelProperty(value = "笔记类型名")
    private String foodNoteTypeName;

    /**
     * 笔记类型ID
     */
    @ApiModelProperty(value = "笔记类型id")
    private Long foodNoteTypeId;

    /**
     * 笔记点赞数
     */
    @ApiModelProperty(value = "笔记点赞数")
    private Integer foodNoteLikeNum;

    /**
     * 笔记评论数
     */
    @ApiModelProperty(value = "笔记评论数")
    private Integer foodNoteCommentNum;

    /**
     * 笔记收藏数
     */
    @ApiModelProperty(value = "笔记收藏数")
    private Integer foodNoteCollectNum;

    /**
     * 浏览阅读数
     */
    @ApiModelProperty(value = "笔记阅读数")
    private Integer foodNoteReadingNum;

    /**
     * 状态 false-草稿箱 true-已发布
     */
    private Boolean status;

    /**
     * 笔记封面
     */
    @ApiModelProperty(value = "笔记封面，选用第一张作为封面")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String coverImg;

}
