package com.iotinall.canteen.dto.foodnotecommentrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 笔记评论添加请求参数
 * @author: JoeLau
 * @time: 2021年07月08日 11:58:36
 */

@Data
@ApiModel(value = "笔记评论添加请求参数")
public class FoodNoteCommentRecordAddReq {
    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容", notes = "comment")
    @NotBlank(message = "评论内容不能为空")
    private String comment;

    /**
     * 上条评论ID
     */
    @ApiModelProperty(value = "上条评论（父评论）的ID")
    private Long lastId;

    /**
     * 美食笔记
     */
    @ApiModelProperty(value = "美食笔记ID")
    @NotNull(message = "美食笔记不能为空")
    private Long foodNoteId;
}
