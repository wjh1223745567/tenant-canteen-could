package com.iotinall.canteen.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品原料 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 10:28
 */
@Data
@Entity
@Table(name = "dish_material")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SysDishMaterial {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @OneToOne
    @JoinColumn(name = "dish_id", foreignKey = @ForeignKey(name = "null"))
    private SysDish dish;

    @OneToOne
    @JoinColumn(name = "material_id", foreignKey = @ForeignKey(name = "null"))
    private SysMaterial material;

    /**
     * 主料,辅料,调料
     */
    private Integer master;
    /**
     * 原料的量/
     */
    private BigDecimal amount;

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
