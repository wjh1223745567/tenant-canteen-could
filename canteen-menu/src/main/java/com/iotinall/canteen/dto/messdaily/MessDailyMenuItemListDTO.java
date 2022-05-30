package com.iotinall.canteen.dto.messdaily;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MessDailyMenuItemListDTO {
    @ApiModelProperty(value = "菜品id")
    private Long id;

    @ApiModelProperty(value = "菜品名称")
    private String name;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "菜品图片")
    private String img;

    @ApiModelProperty(value = "好评数")
    private Integer praiseCount;

    @ApiModelProperty(value = "差评数")
    private Integer negativeCount;

    @ApiModelProperty(value = "厨师ID")
    private Long cookId;

    @ApiModelProperty(value = "厨师名称")
    private String cookName;

    @ApiModelProperty(value = "厨师角色")
    private String cookRole;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "厨师头像")
    private String cookAvatar;

    @ApiModelProperty(value = "五星数")
    private BigDecimal fiveStar;

    @ApiModelProperty(value = "四星数")
    private BigDecimal fourStar;

    @ApiModelProperty(value = "三星数")
    private BigDecimal threeStar;

    @ApiModelProperty(value = "二星数")
    private BigDecimal twoStar;

    @ApiModelProperty(value = "一星数")
    private BigDecimal oneStar;
}
