package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 字典表
 * @author xin-bing
 * @date 10/22/2019 19:46
 */
@Data
@Entity
@Table(name = "sys_dict_detail")
public class SysDictDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键

    @Column(nullable = false)
    private String label; // 展示的值

    @Column(nullable = false)
    private String value; // value

    @Column(nullable = false)
    private Integer sort = 0; // 排序

    @Column(nullable = false, updatable = false)
    private Integer typ; // 字典类型， 1为系统默认的，不允许删除，

    @Column(nullable = false, length = 20)
    private String groupCode; // group

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    private String remark;
}