package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long>, JpaSpecificationExecutor<RoomReservation> {
}
