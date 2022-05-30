package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.EquipmentEmployeeRelation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentEmployeeRepository extends JpaRepositoryEnhance<EquipmentEmployeeRelation, Long>, JpaSpecificationExecutor<EquipmentEmployeeRelation> {
    EquipmentEmployeeRelation findByEquIdAndAndEmpId(Long equId, Long empId);
}
