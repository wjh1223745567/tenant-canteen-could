package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 中国居民膳食能量参考摄入量 作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/04/02 15:53
 */
@Data
@Entity
@Table(name = "reference_energy")
public class SysReferenceEnergy {
    @Id
    private String id;

    /**
     * 年龄、生理状态类型 0表示年龄 1表示怀孕 2表示乳母
     */
    private Integer type;
    /**
     * 年龄区间开始值（包含）
     */
    private Long ageStart;
    /**
     * 年龄区间截止值（不包含）
     */
    private Long ageEnd;
    /**
     * 性别 0表示男性 1表示女性
     */
    private Integer sex;
    /**
     * 活动强度 0表示轻度 1表示中度 2表示重度
     */
    private Integer strengthLevel;
    /**
     * 每日能量需要量 单位千卡 0表示未制定
     */
    private Long eer;
    private String description;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
}
