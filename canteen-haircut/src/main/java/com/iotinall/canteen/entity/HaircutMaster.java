package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;


/**
 * @description:理发师
 * @author: JoeLau
 * @time: 2021年06月23日 14:06:44
 */
@Data
@Entity
@Table(name = "haircut_master")
@EqualsAndHashCode(callSuper = true)
public class HaircutMaster extends BaseEntity {

    /**
     * 理发师姓名
     */
    private String name;

    /**
     * 理发师联系方式
     */
    private String phoneNumber;

    /**
     * 所属理发室
     */
    @ManyToOne
    @JoinColumn(name = "haircut_room_id")
    private HaircutRoom haircutRoom;

    /**
     * 参加工作日期
     */
    private LocalDate workingStart;

    /**
     * 理发师介绍
     */
    private String presentation;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否已删除
     */
    private boolean deleted;

    /**
     * 人员ID
     */
    private Long empId;
}
