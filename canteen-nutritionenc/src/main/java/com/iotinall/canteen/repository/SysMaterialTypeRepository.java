package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysMaterialType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 原料类型 Repository
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysMaterialTypeRepository extends JpaRepositoryEnhance<SysMaterialType, String>, JpaSpecificationExecutor<SysMaterialType> {
}