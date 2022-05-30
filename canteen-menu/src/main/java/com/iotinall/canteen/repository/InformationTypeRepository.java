package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.InformationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author WJH
 * @date 2019/11/19:44
 */
public interface InformationTypeRepository extends JpaRepository<InformationType, Long>, JpaSpecificationExecutor<InformationType> {
    @Modifying
    @Query("update InformationType set infoCount = infoCount + :count where id = :id")
    int addInfoCount(@Param("id") Long id, @Param("count") int count);

    /**
     * 根据组织机构获取类型列表
     *
     * @author loki
     * @date 2020/04/30 10:01
     */
    @Query(value = "select i.* from information_type i where find_in_set(:orgId,i.receive_org)", nativeQuery = true)
    List<InformationType> queryByOrg(@Param("orgId") Long orgId);

    /**
     * add by liujun 进销存添加检测报告生成资讯，资讯的类型为“检测报告”,每次生成资讯时通过名称查找资讯类型，其他地方误用
     */
    InformationType queryByName(String name);
}
