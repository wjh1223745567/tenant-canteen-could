package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.RoomReservation;
import com.iotinall.canteen.entity.RoomReservationReserved;
import com.iotinall.canteen.entity.RoomReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface RoomReservationReservedRepository extends JpaRepository<RoomReservationReserved, Long>, JpaSpecificationExecutor<RoomReservationReserved> {

    List<RoomReservationReserved> findByRoomAndReservedTimeAndType(RoomReservation roomReservation, LocalDate date, Integer type);

    List<RoomReservationReserved> findByRoomAndReservedTime(RoomReservation roomReservation, LocalDate date);

    List<RoomReservationReserved> findByRoom(RoomReservation roomReservation);

    List<RoomReservationReserved> findByRoomAndType(RoomReservation roomReservation, Integer type);

    List<RoomReservationReserved> findByRoomAndReservedTimeAndTime(RoomReservation roomReservation,
                                                                   LocalDate localDate,
                                                                   RoomReservationTime time);

    Integer countByRoomAndReservedTimeAndTime(RoomReservation roomReservation,
                                                                   LocalDate localDate,
                                                                   RoomReservationTime time);
}
