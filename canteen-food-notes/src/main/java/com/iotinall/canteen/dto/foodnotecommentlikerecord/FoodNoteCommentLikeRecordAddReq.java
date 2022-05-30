package com.iotinall.canteen.dto.foodnotecommentlikerecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 评论点赞记录添加请求
 * @author: JoeLau
 * @time: 2021年07月06日 17:32:35
 */

@Data
@ApiModel(value = "评论点赞记录添加请求")
public class FoodNoteCommentLikeRecordAddReq {
    /**
     * 点赞人ID
     */
    @ApiModelProperty(value = "点赞人用户ID")
    private Long userId;

    /**
     * 评论ID
     */
    @ApiModelProperty(value = "评论ID")
    private Long commentId;
}
