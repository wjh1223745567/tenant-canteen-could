package com.iotinall.canteen.dto.information;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author WJH
 * @date 2019/11/1518:08
 */
@Data
public class InformationCommentAdd {

    @ApiModelProperty(value = "评论内容", name = "content")
    @NotBlank
    @Length(max = 1000, message = "评论字符长度不能超过1000")
    private String content;

    @ApiModelProperty(value = "咨询ID", name = "infoId")
    @NotNull
    private Long infoId;

    @ApiModelProperty(value = "是否匿名", name = "anonymous")
    private Boolean anonymous;

}
