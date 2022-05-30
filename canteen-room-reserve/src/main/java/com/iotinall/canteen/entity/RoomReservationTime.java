package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iotinall.canteen.common.constant.MealTypeEnum;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "room_reservation_time")
@SQLDelete(sql = "update room_reservation_time set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
@Accessors(chain = true)
public class RoomReservationTime extends BaseEntity {
    /**
     * 餐次
     */
    private MealTypeEnum typeEnum;

    @Column(nullable = false)
    private String beginTime;

    @Column(nullable = false)
    private String endTime;

    /**
     * 节假日是否营业
     */
    @Column(nullable = false)
    private Boolean holiday;

    @OneToMany(mappedBy = "time", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RoomReservationReserved> roomReserveds;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    private RoomReservation room;

    @Column(nullable = false)
    private Boolean deleted;
}
