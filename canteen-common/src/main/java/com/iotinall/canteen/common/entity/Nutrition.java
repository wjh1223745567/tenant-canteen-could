package com.iotinall.canteen.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 营养元素
 *
 * @author loki
 * @date 2020/04/14 11:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class Nutrition extends BaseEntity {
    /**
     * 能量
     */
    @Column(name = "energy")
    private Long energy;
    /**
     * 蛋白质
     */
    @Column(name = "protein")
    private Long protein;
    /**
     * 脂肪
     */
    @Column(name = "fat")
    private Long fat;
    /**
     * 碳水化合物
     */
    @Column(name = "carbohydrate")
    private Long carbohydrate;

    /**
     * 叶酸
     */
    @Column(name = "vb9")
    private Long vb9;
    /**
     * 膳食纤维
     */
    @Column(name = "dietary_fiber")
    private Long dietaryFiber;
    /**
     * 维生素A
     */
    @Column(name = "va")
    private Long va;
    /**
     * 维生素C
     */
    @Column(name = "vc")
    private Long vc;
    /**
     * 维生素D
     */
    @Column(name = "vd")
    private Long vd;
    /**
     * 维生素E
     */
    @Column(name = "ve")
    private Long ve;
    /**
     * 维生素K
     */
    @Column(name = "vk")
    private Long vk;
    /**
     * 维生素B1
     */
    @Column(name = "vb1")
    private Long vb1;
    /**
     * 维生素B2
     */
    @Column(name = "vb2")
    private Long vb2;
    /**
     * 维生素B6
     */
    @Column(name = "vb6")
    private Long vb6;
    /**
     * 维生素B12
     */
    @Column(name = "vb12")
    private Long vb12;
    /**
     * 泛酸
     */
    @Column(name = "pan_acid")
    private Long panAcid;
    /**
     * 膳食叶酸当量
     */
    @Column(name = "dfe")
    private Long dfe;
    /**
     * 生物素
     */
    @Column(name = "vh")
    private Long vh;
    /**
     * 胡萝卜素
     */
    @Column(name = "carotene")
    private Long carotene;
    /**
     * 核黄素
     */
    @Column(name = "riboflavin")
    private Long riboflavin;
    /**
     * 硫胺素
     */
    @Column(name = "thiamine")
    private Long thiamine;
    /**
     * 烟酸
     */
    @Column(name = "niacin")
    private Long niacin;
    /**
     * 烟酸当量
     */
    @Column(name = "niacin_equivalent")
    private Long niacinEquivalent;
    /**
     * 胆碱
     */
    @Column(name = "choline")
    private Long choline;
    /**
     * 胆固醇
     */
    @Column(name = "cholesterol")
    private Long cholesterol;
    /**
     * 视黄醇当量
     */
    @Column(name = "re")
    private Long re;
    /**
     * 钙
     */
    @Column(name = "ca")
    private Long ca;
    /**
     * 磷
     */
    @Column(name = "p")
    private Long p;
    /**
     * 钾
     */
    @Column(name = "k")
    private Long k;
    /**
     * 钠
     */
    @Column(name = "na")
    private Long na;
    /**
     * 镁
     */
    @Column(name = "mg")
    private Long mg;
    /**
     * 碘
     */
    @Column(name = "i")
    private Long i;
    /**
     * 铁
     */
    @Column(name = "fe")
    private Long fe;
    /**
     * 锌
     */
    @Column(name = "zn")
    private Long zn;
    /**
     * 硒
     */
    @Column(name = "se")
    private Long se;
    /**
     * 铜
     */
    @Column(name = "cu")
    private Long cu;
    /**
     * 锰
     */
    @Column(name = "mn")
    private Long mn;
    /**
     * 铬
     */
    @Column(name = "cr")
    private Long cr;
    /**
     * 钼
     */
    @Column(name = "mo")
    private Long mo;
    /**
     * 氯
     */
    @Column(name = "cl")
    private Long cl;
}
