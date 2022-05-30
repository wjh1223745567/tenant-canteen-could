package com.iotinall.canteen.dto.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 添加评论
 * @author WJH
 * @date 2019/11/610:54
 */
@Setter
@Getter
@ApiModel(description = "添加评论")
public class MessProductCommentAddReq {

    @ApiModelProperty(value = "菜品ID", name = "productId", required = true)
    @NotNull(message = "菜品ID不能为空")
    private Long productId;

    @ApiModelProperty(value = "星星", name = "score", required = true)
    @NotNull
    private BigDecimal score;

    @ApiModelProperty(value = "所选择的标签", name = "tags")
    @NotEmpty(message = "请选择标签")
    private String tags;

    @ApiModelProperty(value = "评价内容 不能超过255个字符", name = "content", required = true)
    @Length(max = 255, message = "评价内容不能超过255个字符")
    private String content;

    @ApiModelProperty(value = "是否时匿名评论", name = "anonymous")
    private Boolean anonymous;

}
