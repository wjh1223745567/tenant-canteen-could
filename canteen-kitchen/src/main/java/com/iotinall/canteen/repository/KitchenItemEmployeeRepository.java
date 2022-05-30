package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenItemEmployee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface KitchenItemEmployeeRepository extends JpaRepositoryEnhance<KitchenItemEmployee, Long>, JpaSpecificationExecutor<KitchenItemEmployee> {
    List<KitchenItemEmployee> findByItem(KitchenItem item);
}
