package com.iotinall.canteen.repository;


import com.iotinall.canteen.entity.RoomReservationReserved;
import com.iotinall.canteen.entity.RoomReservationReservedProd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoomReservationReservedProdRepository extends JpaRepository<RoomReservationReservedProd, Long>, JpaSpecificationExecutor<RoomReservationReservedProd> {
    List<RoomReservationReservedProd> findByReserved(RoomReservationReserved reserved);
}
