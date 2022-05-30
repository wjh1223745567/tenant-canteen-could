package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.SysDictDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * sys_dict_detail Repository
 *
 * @author xin-bing
 * @date 2019-10-23 11:35:24
 */
public interface SysDictDetailRepository extends JpaRepository<SysDictDetail, Long>, JpaSpecificationExecutor<SysDictDetail> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    @Query(value = "delete from SysDictDetail where id in :ids")
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    SysDictDetail findByGroupCodeAndValue(String groupCode, String value);

    List<SysDictDetail> findByGroupCodeIn(List<String> groupCodes);
}