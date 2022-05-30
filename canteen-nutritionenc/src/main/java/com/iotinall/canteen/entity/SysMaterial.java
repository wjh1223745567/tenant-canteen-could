package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.NutritionNone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 原材料 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 11:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "material")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SysMaterial extends NutritionNone {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String name;
    /**
     * 菜品图片路径
     */
    private String img;

    /**
     * 原料类型
     */
    @OneToOne
    @JoinColumn(name = "material_type_id", foreignKey = @ForeignKey(name = "null"))
    private SysMaterialType materialType;

    /**
     * 别名
     */
    private String alias;

    /**
     * 用于后台检索
     */
    private String search;

    /**
     * 原料使用提示
     */
    private String tips;
    /**
     * 原料介绍
     */
    private String introduce;
    /**
     * 原料营养分析
     */
    private String nutritionAnalysis;
    /**
     * 原料适用人群
     */
    private String forPeople;
    /**
     * 相克原料
     */
    private String restriction;
    /**
     * 原料制作指导
     */
    private String guidance;
    /**
     * 原料食疗作用
     */
    private String dietaryTherapy;

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
