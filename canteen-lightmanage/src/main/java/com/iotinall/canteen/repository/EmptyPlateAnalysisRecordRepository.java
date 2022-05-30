package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.EmptyPlateAnalysisRecord;
import com.iotinall.canteen.entity.EmptyPlateImgRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 光盘行动识别记录 Repository
 *
 * @author loki
 * @date 2019-10-30 10:20:51
 */
public interface EmptyPlateAnalysisRecordRepository extends JpaRepositoryEnhance<EmptyPlateAnalysisRecord, Long>, JpaSpecificationExecutor<EmptyPlateAnalysisRecord> {
    /**
     * 通过违规记录ID获取所有识别记录
     *
     * @author loki
     * @date 2021/05/26 14:50
     */
    List<EmptyPlateAnalysisRecord> queryByImgRecord(EmptyPlateImgRecord imgRecord);
}
