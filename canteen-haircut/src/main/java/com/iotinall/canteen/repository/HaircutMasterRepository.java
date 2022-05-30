package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.HaircutMaster;
import com.iotinall.canteen.entity.HaircutRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HaircutMasterRepository extends JpaRepository<HaircutMaster, Long>, JpaSpecificationExecutor<HaircutMaster> {
    HaircutMaster findByIdAndDeleted(Long id, boolean deleted);

    List<HaircutMaster> findAllByHaircutRoom(HaircutRoom room);

    @Query(value = "from HaircutMaster r where deleted =:deleted and haircutRoom =:room")
    List<HaircutMaster> findAllByDeletedAndHaircutRoom(boolean deleted, HaircutRoom room);

    HaircutMaster findByEmpIdAndAndDeleted(Long empId,boolean deleted);
}
