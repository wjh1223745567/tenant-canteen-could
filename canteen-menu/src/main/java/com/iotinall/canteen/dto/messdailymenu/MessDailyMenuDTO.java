package com.iotinall.canteen.dto.messdailymenu;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


/**
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "返回mess_daily_menu结果")
public class MessDailyMenuDTO implements Serializable {

    @ApiModelProperty(value = "主键，返回的为null说明没有创建菜谱", required = true)
    private Long id;// id

    @ApiModelProperty(value = "日期", required = true)
    private LocalDate menuDate;// menu_date

    @ApiModelProperty(value = "早餐")
    private List<MenuProd> breakfast;

    @ApiModelProperty(value = "午餐")
    private List<MenuProd> lunch;

    @ApiModelProperty(value = "晚餐")
    private List<MenuProd> dinner;

    @ApiModelProperty(value = "备注")
    private String remark;// remark

    @ApiModelProperty(value = "当前时段餐次类型")
    private Integer mealType;

    @Getter
    @Setter
    public static class MenuProd {
        @ApiModelProperty(value = "菜品id")
        private Long id;
        @ApiModelProperty(value = "菜品名称")
        private String name;

        @ApiModelProperty(value = "列表ID")
        private Long itemId;

        @ApiModelProperty(value = "评分")
        private Long score;

        @ApiModelProperty(value = "菜品归类")
        private Integer dishClass;

        /**
         * 厨师ID
         */
        private Long cookId;

        @ApiModelProperty(value = "厨师姓名")
        private String cookName;

        @ApiModelProperty(value = "厨师头像")
        @JsonSerialize(using = ImgPair.ImgSerializer.class)
        private String cookImg;

        @JsonSerialize(using = ImgPair.ImgSerializer.class)
        @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
        @ApiModelProperty(value = "菜品图片")
        private String img;

        @Override
        public int hashCode() {
            return (int) (id & 2 ^ 31);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            if (o instanceof MenuProd) {
                MenuProd another = (MenuProd) o;
                return getId().equals(another.getId());
            }
            return false;
        }
    }
}