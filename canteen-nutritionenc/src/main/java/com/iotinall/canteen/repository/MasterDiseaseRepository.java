package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MasterDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 疾病类型持久化类
 *
 * @author loki
 * @date 2020/12/09 19:41
 */
public interface MasterDiseaseRepository extends JpaRepository<MasterDisease, Long>, JpaSpecificationExecutor<MasterDisease> {

}
