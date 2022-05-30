package com.iotinall.canteen.dto.foodnote;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 美食笔记添加请求参数
 * @author: JoeLau
 * @time: 2021年07月07日 15:22:18
 */
@Data
@ApiModel(value = "美食笔记添加请求参数")
public class FoodNoteAddReq {

    /**
     * 标题
     */
    @ApiModelProperty(value = "美食笔记标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 笔记类型
     */
    @ApiModelProperty(value = "美食笔记类型ID")
    @NotNull(message = "类型不能为空")
    private Long foodNoteTypeId;

    /**
     * 多张图片，最多九张，用逗号分隔
     */
    @ApiModelProperty(value = "美食笔记图片")
//    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private List<String> pictureList;

    /**
     * 笔记内容
     */
    @ApiModelProperty(value = "美食笔记内容")
    @NotBlank(message = "笔记内容不能为空")
    private String content;

    /**
     * 状态 false-草稿箱 true-已发布
     */
    @ApiModelProperty(value = "美食笔记状态",notes = "true-发布 false-保存到草稿箱")
    @NotNull(message = "笔记发布状态不能为空")
    private Boolean status;
}
