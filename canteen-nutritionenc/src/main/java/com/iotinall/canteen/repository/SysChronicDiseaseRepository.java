package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.ChronicDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 慢性疾病逻辑处理类
 *
 * @author loki
 * @date 2020/12/09 19:41
 */
public interface SysChronicDiseaseRepository extends JpaRepository<ChronicDisease, Long>, JpaSpecificationExecutor<ChronicDisease> {

    ChronicDisease findAllByName(String name);

}
