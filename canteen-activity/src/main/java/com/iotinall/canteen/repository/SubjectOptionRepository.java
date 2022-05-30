package com.iotinall.canteen.repository;

import com.iotinall.canteen.domain.ActivitySubject;
import com.iotinall.canteen.domain.ActivitySubjectOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 题目选项持久化类
 *
 * @author joelau
 * @date 2021/05/25 10:39
 */
public interface SubjectOptionRepository extends JpaRepository<ActivitySubjectOption,Long>, JpaSpecificationExecutor<ActivitySubjectOption> {
    void deleteAllBySubject(ActivitySubject subject);
}
