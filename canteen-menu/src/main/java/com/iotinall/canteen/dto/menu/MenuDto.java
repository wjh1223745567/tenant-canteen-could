package com.iotinall.canteen.dto.menu;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author WJH
 * @date 2019/11/516:34
 */
@Setter
@Getter
@Accessors(chain = true)
public class MenuDto {

    @ApiModelProperty(value = "菜品ID", name = "id")
    private Long id;

    @ApiModelProperty(value = "菜单ID", name = "menuId")
    private Long dailyMenuId;

    @ApiModelProperty(value = "厨师ID", name = "cookId")
    private Long cookId;

    @ApiModelProperty(value = "厨师名称", name = "cookName")
    private String cookName;

    @ApiModelProperty(value = "厨师头像", name = "cookAvatar")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String cookAvatar;

    @ApiModelProperty(value = "菜品名称", name = "name")
    private String name;

    @ApiModelProperty(value = "平均星星", name = "stars")
    private BigDecimal stars;

    @ApiModelProperty(value = "是否已推荐", name = "isRecommend")
    private Boolean isRecommend;

    @ApiModelProperty(value = "是否适合这个人食用,true-适合 false-不适合", name = "suitable")
    private Boolean suitable;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "图片", name = "image")
    private String image;

    @ApiModelProperty(value = "评价数量, 或 推荐数量 ", name = "numberOfEvaluations")
    private long numberOfEvaluations;

    @ApiModelProperty(value = "推荐数量")
    private int recommendCount;

    private Integer dishClass;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(id, menuDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
