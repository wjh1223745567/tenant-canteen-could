package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 预定信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SQLDelete(sql = "update room_reservation_reserved set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
@Table(name = "room_reservation_reserved")
@Accessors(chain = true)
public class RoomReservationReserved extends BaseEntity {
    /**
     * 预定人(申请人)
     */
    private Long applyPeopleId;

    /**
     * 预定人(申请人)名称 冗余便于查询
     */
    private String applyPeopleName;

    /**
     * 确认人
     */
    private Long confirmId;

    //审核意见
    private String confirmRemark;

    /**
     * 预定的餐厅
     */
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    @NotFound(action = NotFoundAction.IGNORE)
    private RoomReservation room;

    @OneToMany(mappedBy = "reserved", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RoomReservationReservedProd> prods;


    /**
     * 预定餐次
     *
     * @see com.iotinall.canteen.common.constant.MealTypeEnum
     */
    private Integer type;

    /**
     * 预定日期
     */
    private LocalDate reservedTime;

    /**
     * 预定时间
     */
    @ManyToOne
    @JoinColumn(name = "time_id", nullable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    @NotFound(action = NotFoundAction.IGNORE)
    private RoomReservationTime time;

    @Column(nullable = false)
    private Boolean deleted;

    /**
     * 确认状态
     *
     * @see com.iotinall.canteen.constant.ToExamineEnum
     */
    @Column(nullable = false)
    private Integer toExamine;

    /**
     * 就餐人数
     */
    private Integer numberOfPeople;

    private String remark;
}
