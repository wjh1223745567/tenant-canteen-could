package com.iotinall.canteen.dto.recommend;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MessProductRecommendDTO {
    private Long id;
    private String name;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    /**
     * 推荐次数
     */
    private Integer recommendCount;

    /**
     * 好评率
     */
    private BigDecimal favourRate;

    /**
     * 本周已排次数
     */
    private Integer weekTimes;

    private Integer dishClass;
}
