package com.iotinall.canteen.dto.dish;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 营养元素
 *
 * @author loki
 * @date 2020/04/14 11:43
 */
@Getter
@Setter
public class NutritionDTO implements Serializable {
    /**
     * 能量
     */
    private Long energy = Long.valueOf(0);
    /**
     * 蛋白质
     */
    private Long protein = Long.valueOf(0);
    /**
     * 脂肪
     */
    private Long fat = Long.valueOf(0);
    /**
     * 碳水化合物
     */
    private Long carbohydrate = Long.valueOf(0);

    /**
     * 叶酸
     */
    private Long vb9 = Long.valueOf(0);
    /**
     * 膳食纤维
     */
    private Long dietaryFiber = Long.valueOf(0);
    /**
     * 维生素A
     */
    private Long va = Long.valueOf(0);
    /**
     * 维生素C
     */
    private Long vc = Long.valueOf(0);
    /**
     * 维生素D
     */
    private Long vd = Long.valueOf(0);
    /**
     * 维生素E
     */
    private Long ve = Long.valueOf(0);
    /**
     * 维生素K
     */
    private Long vk = Long.valueOf(0);
    /**
     * 维生素B1
     */
    private Long vb1 = Long.valueOf(0);
    /**
     * 维生素B2
     */
    private Long vb2 = Long.valueOf(0);
    /**
     * 维生素B6
     */
    private Long vb6 = Long.valueOf(0);
    /**
     * 维生素B12
     */
    private Long vb12 = Long.valueOf(0);
    /**
     * 泛酸
     */
    private Long panAcid = Long.valueOf(0);
    /**
     * 膳食叶酸当量
     */
    private Long dfe = Long.valueOf(0);
    /**
     * 生物素
     */
    private Long vh = Long.valueOf(0);
    /**
     * 胡萝卜素
     */
    private Long carotene = Long.valueOf(0);
    /**
     * 核黄素
     */
    private Long riboflavin = Long.valueOf(0);
    /**
     * 硫胺素
     */
    private Long thiamine = Long.valueOf(0);
    /**
     * 烟酸
     */
    private Long niacin = Long.valueOf(0);
    /**
     * 烟酸当量
     */
    private Long niacinEquivalent = Long.valueOf(0);
    /**
     * 胆碱
     */
    private Long choline = Long.valueOf(0);
    /**
     * 胆固醇
     */
    private Long cholesterol = Long.valueOf(0);
    /**
     * 视黄醇当量
     */
    private Long re = Long.valueOf(0);
    /**
     * 钙
     */
    private Long ca = Long.valueOf(0);
    /**
     * 磷
     */
    private Long p = Long.valueOf(0);
    /**
     * 钾
     */
    private Long k = Long.valueOf(0);
    /**
     * 钠
     */
    private Long na = Long.valueOf(0);
    /**
     * 镁
     */
    private Long mg = Long.valueOf(0);
    /**
     * 碘
     */
    private Long i = Long.valueOf(0);
    /**
     * 铁
     */
    private Long fe = Long.valueOf(0);
    /**
     * 锌
     */
    private Long zn = Long.valueOf(0);
    /**
     * 硒
     */
    private Long se = Long.valueOf(0);
    /**
     * 铜
     */
    private Long cu = Long.valueOf(0);
    /**
     * 锰
     */
    private Long mn = Long.valueOf(0);
    /**
     * 铬
     */
    private Long cr = Long.valueOf(0);
    /**
     * 钼
     */
    private Long mo = Long.valueOf(0);
    /**
     * 氯
     */
    private Long cl = Long.valueOf(0);
}
