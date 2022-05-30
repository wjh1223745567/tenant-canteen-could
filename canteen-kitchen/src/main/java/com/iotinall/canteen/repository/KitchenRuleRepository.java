package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenRule;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KitchenRuleRepository extends JpaRepositoryEnhance<KitchenRule, Long>, JpaSpecificationExecutor<KitchenRule> {

    KitchenRule findByTitle(String title);

    Boolean existsByType(KitchenItem item);
}
