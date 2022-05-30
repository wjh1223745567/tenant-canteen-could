package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.HaircutRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface HaircutRoomRepository extends JpaRepository<HaircutRoom, Long>, JpaSpecificationExecutor<HaircutRoom> {
    HaircutRoom findByLongitudeAndLatitudeAndDeleted(BigDecimal longitude, BigDecimal latitude, boolean deleted);

    HaircutRoom findByIdAndDeleted(Long id, boolean deleted);

    @Query(value = "from HaircutRoom r where deleted =:deleted")
    List<HaircutRoom> findAllByDeleted(boolean deleted);
}
