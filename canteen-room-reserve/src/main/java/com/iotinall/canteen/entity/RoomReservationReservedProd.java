package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 预定菜谱
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "room_reservation_reserved_prod")
@Accessors(chain = true)
public class RoomReservationReservedProd extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "reserve_id", nullable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    private RoomReservationReserved reserved;

    private Long messProductId;

    private String messProductName;
}
