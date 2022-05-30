package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iotinall.canteen.common.entity.Nutrition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * 食堂商品
 *
 * @author xin-bing
 * @date 10/10/2019 20:35
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "mess_product")
@SQLDelete(sql = "update mess_product set deleted = 1 where id = ?")
@Where(clause = " deleted = 0 ")
public class MessProduct extends Nutrition implements Serializable {
    @Column(nullable = false, length = 128)
    private String name; // 商品名称

    @Column(nullable = false)
    private String catalog; // 清单，二进制字符串表示，基础数据：1-早餐，2-午餐，3-晚餐，4-外带

    @Column(nullable = false)
    private Boolean enabled; // 上架/下架

    /**
     * 菜品ID
     */
    private Long sysDishId;

    /**
     * 原材料
     */
    @Column(columnDefinition = "text")
    private String material;

    private String intro; // 描述，营养信息的描述


    private String img; // 图片

    @Column(nullable = false)
    private Integer recommendedCount; // 总推荐次数

    @Column(nullable = false)
    private BigDecimal avgScore; // 平均分

    @Column(nullable = false)
    private BigDecimal favorRate; // 好评率

    @Column(nullable = false)
    private Integer soldCount; // 卖出总数

    private Boolean deleted;

    /********************************************************************************************************/
    //刘俊 修改于2020-03-26
    /**
     * 工艺
     */
    @Column(name = "craft_id")
    private String craftId;
    /**
     * 口味
     */
    @Column(name = "flavour_id")
    private String flavourId;

    /**
     * 口感
     */
    private String taste;

    /**
     * 类别,多选
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "product")
    @JsonIgnore
    private Set<MessProductCuisine> cuisines;

    /**
     * 制作提示
     */
    private String practiceTips;

    /**
     * 健康提示
     */
    private String healthTips;

    /**
     * 食物相克
     */
    private String restriction;

    /**
     * 适宜疾病
     */
    @Column(columnDefinition = "text")
    private String suitableDisease;

    /**
     * 禁忌疾病
     */
    @Column(columnDefinition = "text")
    private String contraindications;

    /**
     * 用途 1-每日菜谱编排 2-烹饪教程发布 3-全部  二进制字符串表示
     */
    private String useFor;

    /**
     * 菜品做法,步骤
     */
    @Column(columnDefinition = "text")
    private String practice;

    /**
     * 菜品归类 1-荤 2-半荤素 3-全素 4-主食 5-其他
     */
    private Integer dishClass;
}
