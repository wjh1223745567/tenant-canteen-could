package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysFlavour;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 菜品口味 Repository
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysFlavourRepository extends JpaRepositoryEnhance<SysFlavour, String>, JpaSpecificationExecutor<SysFlavour> {
}