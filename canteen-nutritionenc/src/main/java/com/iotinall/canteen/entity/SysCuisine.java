package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 菜品类型 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@Data
@Entity
@Table(name = "cuisine")
public class SysCuisine {
    @Id
    private String id;
    private String name;
    private String masterId;
    private String code;
    private Integer seq;
    private String description;
    @Column(nullable = false)
    private LocalDateTime createdTime;
    @Column(nullable = false)
    private LocalDateTime updatedTime;
}
