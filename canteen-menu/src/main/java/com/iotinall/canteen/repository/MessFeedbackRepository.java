package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.MessFeedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

/**
 * mess_feedback Repository
 * @author xin-bing
 * @date 2019-10-27 21:33:22
 */
public interface MessFeedbackRepository extends JpaRepositoryEnhance<MessFeedback, Long>, JpaSpecificationExecutor<MessFeedback> {

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    int deleteByIdIn(@Param(value = "ids") Long[] ids);
}