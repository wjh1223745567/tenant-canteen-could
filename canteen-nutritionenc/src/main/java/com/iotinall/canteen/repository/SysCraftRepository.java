package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysCraft;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 菜品工艺
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysCraftRepository extends JpaRepositoryEnhance<SysCraft, String>, JpaSpecificationExecutor<SysCraft> {
}