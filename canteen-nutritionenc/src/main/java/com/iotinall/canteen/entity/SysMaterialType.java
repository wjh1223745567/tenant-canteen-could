package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 原材料类型 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 11:25
 */
@Data
@Table(name = "material_type")
@Entity
public class SysMaterialType {
    @Id
    private String id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
