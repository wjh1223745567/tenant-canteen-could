package com.iotinall.canteen.dto.mess;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
public interface CommentsDTO {
    Long getId();

    /**
     * 菜名
     */
    String getName();

    /**
     * 图片
     */
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    String getImg();

    /**
     * 食用
     */
    String getCatalog();

    BigDecimal getFiveStar();

    BigDecimal getFourStar();

    BigDecimal getThreeStar();

    BigDecimal getTwoStar();

    BigDecimal getOneStar();
}
