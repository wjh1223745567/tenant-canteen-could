package com.iotinall.canteen.repository;

import com.iotinall.canteen.domain.ActivitySubject;
import com.iotinall.canteen.domain.ActivitySurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 题目持久化类
 *
 * @author joelau
 * @date 2021/05/25 10:39
 */
public interface SubjectRepository extends JpaRepository<ActivitySubject, Long>, JpaSpecificationExecutor<ActivitySubject> {
    void deleteAllBySurvey(ActivitySurvey survey);

    ActivitySubject findBySeqAndSurvey(Integer seq, ActivitySurvey survey);
}
