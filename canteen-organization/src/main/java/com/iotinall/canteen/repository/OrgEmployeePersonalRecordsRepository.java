package com.iotinall.canteen.repository;


import com.iotinall.canteen.entity.OrgEmployeePersonalRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrgEmployeePersonalRecordsRepository extends JpaRepository<OrgEmployeePersonalRecords, Long>, JpaSpecificationExecutor<OrgEmployeePersonalRecords> {

    List<OrgEmployeePersonalRecords> findAllByEmployeeId(Long id);

    Integer deleteAllByEmployeeId(Long id);
}
