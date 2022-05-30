package com.iotinall.canteen.dto.information;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author WJH
 * @date 2019/11/19:52
 */
@Setter
@Getter
public class InformationAddReq {

    @NotNull(message = "请填写标题")
    @Length(max = 255, message = "标题长度不能超过255个字符")
    private String title;
    /**
     * 置顶
     */
    private Boolean sticky;

    private Integer status;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String cover;

    private String content;

    @NotNull(message = "请选择信息类型")
    private Long typeId;

    @ApiModelProperty
    private List<Long> receivers;

    private Integer seq;
}
