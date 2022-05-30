package com.iotinall.canteen.dto.foodnote;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.foodnotecommentrecord.FoodNoteCommentRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @description: 美食笔记详情DTO
 * @author: JoeLau
 * @time: 2021年07月06日 15:38:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "返回美食笔记详情DTO")
public class FoodNoteDetailDTO extends FoodNoteDTO {

    /**
     * 多张图片，最多九张
     */
    @ApiModelProperty(value = "笔记图片")
//    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private List<String> pictureList;

    /**
     * 笔记内容
     */
    @ApiModelProperty(value = "笔记文字内容")
    private String content;

    /**
     * 笔记评论
     */
    @ApiModelProperty(value = "笔记评论")
    private List<FoodNoteCommentRecordDTO> commentRecordDTOList;

    /**
     * 是否关注
     */
    @ApiModelProperty(value = "是否关注")
    private Boolean followStatus;

    /**
     * 是否收藏
     */
    @ApiModelProperty(value = "是否收藏")
    private Boolean collectStatus;

    /**
     * 是否点赞
     */
    @ApiModelProperty(value = "是否点赞")
    private Boolean likeStatus;
}
