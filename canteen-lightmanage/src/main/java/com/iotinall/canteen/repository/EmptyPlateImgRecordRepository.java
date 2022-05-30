package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.EmptyPlateImgRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 光盘行动违规图片记录 Repository
 *
 * @author loki
 * @date 2019-10-30 10:20:51
 */
public interface EmptyPlateImgRecordRepository extends JpaRepositoryEnhance<EmptyPlateImgRecord, Long>, JpaSpecificationExecutor<EmptyPlateImgRecord> {

    @Query(value = "from EmptyPlateImgRecord record where record.analysis is null or record.analysis = 0")
    List<EmptyPlateImgRecord> queryAllRecordNotAnalysis();
}
