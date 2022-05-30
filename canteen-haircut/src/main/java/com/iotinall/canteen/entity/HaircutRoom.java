package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;


/**
 * @description:理发室
 * @author: JoeLau
 * @time: 2021/6/23  14:30
 */

@Data
@Entity
@Table(name = "haircut_room")
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"masterList", "orderList"})
public class HaircutRoom extends BaseEntity {

    /**
     * 理发室名字
     */
    private String name;

    /**
     * 理发室介绍
     */
    private String presentation;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhoneNumber;

    /**
     * 理发室地址
     */
    private String address;

    /**
     * 营业开始时间
     */
    private LocalTime openingTime;

    /**
     * 营业结束时间
     */
    private LocalTime closingTime;

    /**
     * 节假日是否营业
     */
    private Boolean openInHoliday;

    /**
     * 理发室经度
     */
    private BigDecimal longitude;

    /**
     * 理发室纬度
     */
    private BigDecimal latitude;

//    /**
//     * 理发室地图地址
//     */
//    private String mapAddress;

    /**
     * 理发室是否删除
     */
    private boolean deleted;

    /**
     * 理发室的理发师
     */
    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "haircutRoom")
    private List<HaircutMaster> masterList;

    /**
     * 理发室的理发订单
     */
    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "haircutRoom")
    private List<HaircutOrder> orderList;

}
