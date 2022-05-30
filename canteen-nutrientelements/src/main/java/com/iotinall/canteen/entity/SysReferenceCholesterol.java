package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 中国居民膳碳水化合物参考摄入量 作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/04/02 15:53
 */
@Data
@Entity
@Table(name = "reference_cholesterol")
public class SysReferenceCholesterol {
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
     * 碳水化合物的平均摄入量
     */
    private Long choEar;
    /**
     * 碳水化合物摄入的最小占比(能量占比)
     */
    private Long choMin;
    /**
     * 碳水化合物摄入的最大占比（能量占比）
     */
    private Long choMax;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
