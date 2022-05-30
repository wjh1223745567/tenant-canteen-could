package com.iotinall.canteen.dto.dish;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统菜品
 *
 * @author loki
 * @date 2020/04/13 13:39
 */
@Accessors(chain = true)
@Getter
@Setter
@ApiModel(value = "营养档案-营养调理-菜谱")
public class DishSimpleDTO  implements Serializable {
    @ApiModelProperty(value = "菜谱ID")
    private String id;
    @ApiModelProperty(value = "菜谱名称")
    private String name;
    /**
     * 图片
     */
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    /**
     * 能量
     */
    private Long energy;
    /**
     * 蛋白质
     */
    private Long protein;
    /**
     * 脂肪
     */
    private Long fat;
    /**
     * 碳水化合物
     */
    private Long carbohydrate;

    /**
     * 叶酸
     */
    private Long vb9;
    /**
     * 膳食纤维
     */
    private Long dietaryFiber;
    /**
     * 维生素A
     */
    private Long va;
    /**
     * 维生素C
     */
    private Long vc;
    /**
     * 维生素D
     */
    private Long vd;
    /**
     * 维生素E
     */
    private Long ve;
    /**
     * 维生素K
     */
    private Long vk;
    /**
     * 维生素B1
     */
    private Long vb1;
    /**
     * 维生素B2
     */
    private Long vb2;
    /**
     * 维生素B6
     */
    private Long vb6;
    /**
     * 维生素B12
     */
    private Long vb12;
    /**
     * 泛酸
     */
    private Long panAcid;
    /**
     * 膳食叶酸当量
     */
    private Long dfe;
    /**
     * 生物素
     */
    private Long vh;
    /**
     * 胡萝卜素
     */
    private Long carotene;
    /**
     * 核黄素
     */
    private Long riboflavin;
    /**
     * 硫胺素
     */
    private Long thiamine;
    /**
     * 烟酸
     */
    private Long niacin;
    /**
     * 烟酸当量
     */
    private Long niacinEquivalent;
    /**
     * 胆碱
     */
    private Long choline;
    /**
     * 胆固醇
     */
    private Long cholesterol;
    /**
     * 视黄醇当量
     */
    private Long re;
    /**
     * 钙
     */
    private Long ca;
    /**
     * 磷
     */
    private Long p;
    /**
     * 钾
     */
    private Long k;
    /**
     * 钠
     */
    private Long na;
    /**
     * 镁
     */
    private Long mg;
    /**
     * 碘
     */
    private Long i;
    /**
     * 铁
     */
    private Long fe;
    /**
     * 锌
     */
    private Long zn;
    /**
     * 硒
     */
    private Long se;
    /**
     * 铜
     */
    private Long cu;
    /**
     * 锰
     */
    private Long mn;
    /**
     * 铬
     */
    private Long cr;
    /**
     * 钼
     */
    private Long mo;
    /**
     * 氯
     */
    private Long cl;
}
