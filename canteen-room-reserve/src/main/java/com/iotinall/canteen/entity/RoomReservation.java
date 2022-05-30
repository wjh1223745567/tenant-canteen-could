package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constant.RoomTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "room_reservation")
@SQLDelete(sql = "update room_reservation set deleted = 1 where id = ?")
@Where(clause = "deleted = 0")
@Accessors(chain = true)
public class RoomReservation extends BaseEntity {
    /**
     * 包间名称
     */
    @Column(nullable = false)
    private String roomName;

    /**
     * 容纳人数
     */
    @Column(nullable = false)
    private Integer numberOfPeople;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<RoomReservationTime> roomTimes;

    /**
     * 包间地址
     */
    @Column(nullable = false)
    private String roomAddress;

    /**
     * 包间图片
     */
    private String roomPicture;

    /**
     * 包间状态
     */
    @Column(nullable = false)
    private RoomTypeEnum roomTypeEnum;

    @Column(nullable = false)
    private Boolean deleted;

    /**
     * 所属餐厅
     */
    private Long tenantOrgId;
}
