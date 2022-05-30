package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 中国居民膳食蛋白质参考摄入量 作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/04/02 15:53
 */
@Data
@Entity
@Table(name = "reference_protein")
public class SysReferenceProtein {
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
     * 蛋白质平均摄入量
     */
    private Long proteinEar;
    /**
     * 蛋白质推荐摄入量
     */
    private Long proteinRni;

    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
