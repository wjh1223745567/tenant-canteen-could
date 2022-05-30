package com.iotinall.canteen.dto.cookrecord;

import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
public interface CommentsDTO {
    Long getId();

    BigDecimal getFiveStar();

    BigDecimal getFourStar();

    BigDecimal getThreeStar();

    BigDecimal getTwoStar();

    BigDecimal getOneStar();
}