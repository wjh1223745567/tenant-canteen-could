package com.iotinall.canteen.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 主菜系表 存储菜系所属主菜系 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 11:08
 */
@Data
@Entity
@Table(name = "master_cuisine")
public class SysMasterCuisine {
    @Id
    private String id;
    private String name;
    private String code;
    private Integer seq;

    @OneToMany(mappedBy = "masterId")
    private Set<SysCuisine> cuisines;

    private String description;
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
