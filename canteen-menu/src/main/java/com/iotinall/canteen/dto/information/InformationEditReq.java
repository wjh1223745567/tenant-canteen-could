package com.iotinall.canteen.dto.information;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InformationEditReq {
    private Long id;

    @NotNull(message = "请填写标题")
    @Length(max = 255, message = "标题长度不能超过255个字符")
    private String title;

    private Integer status;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String cover;
    /**
     * 置顶
     */
    private Boolean sticky;

    private String content;

    @NotNull(message = "请选择信息类型")
    private Long typeId;

    @ApiModelProperty
    private List<Long> receivers;

    /**
     * 排序
     */
    private Integer seq;
}