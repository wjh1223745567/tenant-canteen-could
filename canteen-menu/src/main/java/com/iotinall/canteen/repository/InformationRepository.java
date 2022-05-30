package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * @author WJH
 * @date 2019/11/19:43
 */
public interface InformationRepository extends JpaRepositoryEnhance<Information, Long>, JpaSpecificationExecutor<Information> {
    Information queryByBusinessId(String bizId);

    @Query(value = "select max(r.seq) from Information r")
    Integer getMaxSeq();

    /**
     * 赞
     *
     * @param empId
     */
    @Modifying
    @Query("update Information o set o.praiseCount = o.praiseCount+1,o.praiseEmpId = concat(coalesce(o.praiseEmpId,''), ',', :empId) where o.id=:commId ")
    void addPraise(@Param("commId") Long commId, @Param("empId") Long empId);

    @Query(value = "select i.* from information i where  i.status = 1 and i.type_id = :typeId  order by i.sticky desc,  i.seq asc ,i.create_time desc",
            nativeQuery = true, countProjection = "i.id")
    Page<Information> pageByTypeId(@Param("typeId") Long typeId, Pageable pageable);

    @Query("from Information i where i.status = 1 and i.businessId is null and i.type.id in (:typeList) order by i.sticky desc,  i.seq asc")
    Page<Information> findTop5OrderByStickyDescSeqAsc(@Param("typeList") Set<Long> typeList, Pageable pageable);

    @Query(value = "select i.* from information i where  (i.status = 1 and i.business_id is null) and i.type_id in (:typeList) order by i.sticky desc,  i.seq asc",
            nativeQuery = true, countProjection = "i.id")
    Page<Information> queryByType(@Param("typeList") Set<Long> typeList, Pageable pageable);
}
