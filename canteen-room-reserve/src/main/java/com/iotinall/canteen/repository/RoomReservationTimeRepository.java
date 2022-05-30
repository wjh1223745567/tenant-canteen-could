package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.constant.MealTypeEnum;
import com.iotinall.canteen.entity.RoomReservation;
import com.iotinall.canteen.entity.RoomReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoomReservationTimeRepository extends JpaRepository<RoomReservationTime, Long>, JpaSpecificationExecutor<RoomReservationTime> {

    List<RoomReservationTime> findByRoomAndTypeEnum(RoomReservation room, MealTypeEnum type);

    RoomReservationTime findByIdAndRoom(Long id,RoomReservation room);

    List<RoomReservationTime> findByRoom(RoomReservation room);

}
