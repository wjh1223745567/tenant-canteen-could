package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @description:理发订单
 * @author: JoeLau
 * @time: 2021年06月23日 14:49:49
 */

@Data
@Entity
@Table(name = "haircut_order")
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"haircutRoom", "haircutMaster"})
public class HaircutOrder extends BaseEntity {

    /**
     * 预约号码
     */
    private String orderNumber;

    /**
     * 所属理发室
     */
    @ManyToOne
    @JoinColumn(name = "haircut_room_id")
    private HaircutRoom haircutRoom;

    /**
     * 所属理发师
     */
    @ManyToOne
    @JoinColumn(name = "haircut_master_id")
    private HaircutMaster haircutMaster;

    /**
     *所属顾客ID
     */
    private Long empId;

    /**
     * 顾客姓名
     */
    private String empName;

    /**
     * 顾客电话
     */
    private String empPhone;

    /**
     * 取号时间
     */
    private LocalDateTime pickTime;

    /**
     * 开始理发时间
     */
    private LocalDateTime startCutTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishedTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 过号时间
     */
    private LocalDateTime passedTime;

    /**
     * 订单状态  0-待服务  1-已取消  2-服务中  3-已完成  4-已过号
     */
    private Integer status;

//    /**
//     * 订单消费金额
//     */
//    private Long consumption;
}
