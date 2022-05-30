package com.iotinall.canteen.repository;


import com.iotinall.canteen.domain.ActivitySurvey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 活动调查持久化类
 *
 * @author joelau
 * @date 2021/05/25 10:39
 */
public interface ActivitySurveyRepository extends JpaRepository<ActivitySurvey, Long>, JpaSpecificationExecutor<ActivitySurvey> {

    /**
     * 根据组织机构获取调查活动列表
     * sql的find_in_set函数
     *
     * @author joelau
     * @date 2020/04/30 10:01
     */
    @Query(value = "select * from activity_survey i where  find_in_set(:orgId,i.org_id_list) and type =:type and state = 1", nativeQuery = true)
    Page<ActivitySurvey> queryByOrg(@Param("orgId") Long orgId, @Param("type") Integer type, Pageable page);

    /**
     * @return
     * @Author JoeLau
     * @Description #根据类型获取所有调查活动列表
     * @Date 2021/8/10  10:07
     * @Param
     */
    @Query(value = "select * from activity_survey i where type =:type and state = 1", nativeQuery = true)
    Page<ActivitySurvey> queryByType(@Param("type")Integer type, Pageable pageable);
}
