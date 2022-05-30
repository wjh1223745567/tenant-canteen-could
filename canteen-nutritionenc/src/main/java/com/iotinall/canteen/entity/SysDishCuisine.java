package com.iotinall.canteen.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 菜品菜系关系表 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 10:25
 */
@Data
@Entity
@Table(name = "dish_cuisine")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SysDishCuisine {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @OneToOne
    @JoinColumn(name = "dish_id", foreignKey = @ForeignKey(name = "null"))
    private SysDish dish;

    @OneToOne
    @JoinColumn(name = "cuisine_id", foreignKey = @ForeignKey(name = "null"))
    private SysCuisine cuisine;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
