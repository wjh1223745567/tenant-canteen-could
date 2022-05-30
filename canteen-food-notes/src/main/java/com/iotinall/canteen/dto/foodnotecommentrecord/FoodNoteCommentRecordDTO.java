package com.iotinall.canteen.dto.foodnotecommentrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 返回笔记评论DTO
 * @author: JoeLau
 * @time: 2021年07月06日 16:23:28
 */

@Data
@ApiModel(value = "返回笔记评论DTO")
public class FoodNoteCommentRecordDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "评论时间")
    private LocalDateTime createTime;

    /**
     * 评论人ID
     */
    private Long userId;

    /**
     * 评论人姓名
     */
    @ApiModelProperty(value = "评论人姓名")
    private String userName;

    /**
     * 评论人头像
     */
    @ApiModelProperty(value = "评论人头像")
    private String userAvatar;

    /**
     * 上条评论ID
     */
    @ApiModelProperty(value = "上条评论ID")
    private Long lastId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "该评论内容")
    private String comment;

    /**
     * 评论点赞数
     */
    @ApiModelProperty(value = "评论点赞数")
    private Integer commentLikeNum;

    /**
     * 评论的回复评论
     */
    @ApiModelProperty(value = "该评论下的回复评论")
    private List<FoodNoteCommentRecordDTO> commentRecordDTOList;
}
