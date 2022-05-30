package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 口味 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 11:05
 */
@Data
@Entity
@Table(name = "flavour")
public class SysFlavour {
    @Id
    private String id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
