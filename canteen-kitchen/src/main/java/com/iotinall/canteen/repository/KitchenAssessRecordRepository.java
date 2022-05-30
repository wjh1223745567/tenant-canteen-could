package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenAssessRecord;
import com.iotinall.canteen.entity.KitchenItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface KitchenAssessRecordRepository extends JpaRepositoryEnhance<KitchenAssessRecord, Long>, JpaSpecificationExecutor<KitchenAssessRecord> {
    Boolean existsByItem(KitchenItem item);

    @Query("select o from KitchenAssessRecord o where o.empId = ?1 and o.typ = ?2 and o.beginDate = ?3 and o.endDate = ?4")
    KitchenAssessRecord findByUnique(Long empId, Integer typ, LocalDate beginDate, LocalDate endDate);
}
