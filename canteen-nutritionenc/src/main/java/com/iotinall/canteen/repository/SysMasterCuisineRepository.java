package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysMasterCuisine;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 菜品类别
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysMasterCuisineRepository extends JpaRepositoryEnhance<SysMasterCuisine, String>, JpaSpecificationExecutor<SysMasterCuisine> {
    /**
     * 根据分类code 获取分类
     *
     * @author loki
     * @date 2020/04/13 20:13
     */
    SysMasterCuisine findAllByCode(String code);
}