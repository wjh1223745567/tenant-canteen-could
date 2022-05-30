package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 菜单 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@Getter
@Setter
@Entity
@Table(name = "dish")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SysDish extends NutritionNone {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String name;

    /**
     * 菜品工艺
     */
    @OneToOne
    @JoinColumn(name = "craft_id", foreignKey = @ForeignKey(name = "null"))
    private SysCraft craft;

    /**
     * 菜品口味
     */
    @OneToOne
    @JoinColumn(name = "flavour_id", foreignKey = @ForeignKey(name = "null"))
    private SysFlavour flavour;

    /**
     * 类别
     */
    @ManyToMany
    @JoinTable(name = "dish_cuisine", joinColumns = {
            @JoinColumn(name = "dish_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "cuisine_id", referencedColumnName = "id")
    })
    @JsonIgnore
    private Set<SysCuisine> cuisines;

    /**
     * 菜品图片路径
     */
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img;

    /**
     * 菜品适合早餐
     */
    private boolean breakfast;

    /**
     * 菜品适合午餐
     */
    private boolean lunch;
    /**
     * 菜品适合晚餐
     */
    private boolean dinner;
    /**
     * 菜品适合零食
     */
    private boolean snacks;

    /**
     * 菜品的口感
     */
    private String taste;
    /**
     * 推荐指数
     */
    private Integer recommendIndex;

    /**
     * 营养指数
     */
    private Integer nutritionIndex;
    /**
     * 难易指数
     */
    private Integer difficultyIndex;
    /**
     * 时间指数
     */
    private Integer timeIndex;
    /**
     * 减肥指数
     */
    private Integer reduceWeightIndex;
    /**
     * 美颜指数
     */
    private Integer beautyIndex;
    /**
     * 麻辣指数
     */
    private Integer spicyIndex;
    /**
     * 菜品的做法
     */
    private String practice;

    /**
     * 食物相克
     */
    private String restriction;

    /**
     * 菜品的制作提示
     */
    private String practiceTips;
    /**
     * 菜品的健康提示
     */
    private String healthTips;

    /**
     * 营养信息
     */
    private String intro;

    /**
     * 备注
     */
    private String description;
    /**
     * 创建时间
     */
    @CreationTimestamp
    private LocalDateTime createdTime;
    /**
     * 更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
