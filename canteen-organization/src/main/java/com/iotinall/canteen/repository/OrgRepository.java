package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.Org;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 组织 Repository
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
public interface OrgRepository extends JpaRepositoryEnhance<Org, Long>, JpaSpecificationExecutor<Org> {
    /**
     * 根据名称查找
     * @param name
     * @return
     */
    List<Org> findByNameAndTenantIdIn(String name, Set<Long> orgIds);

    /**
     * 根据姓名和父ID查询
     *
     * @param name
     * @param pid
     * @return
     */
    Org findFirstByNameAndParentId(String name, Long pid);

    /**
     * 查询第一个父根节点
     *
     * @return
     */
    Org findFirstByParentIdIsNull();

    /**
     * 存在子组织
     *
     * @param id
     * @return
     */
    @Query(value = "select count(p) from Org p where p.parentId = :id")
    long countChildren(@Param(value = "id") Long id);

    /**
     * 获取子组织
     *
     * @param id
     * @return
     */
    List<Org> findByParentId(Long id);

    @Modifying
    @Query(value = "update Org p set p.empCount = p.empCount + :count where p.id in :ids")
    int addEmpCount(@Param("count") int count, @Param("ids") List<Long> ids);

    @EntityGraph(value = "org.findOrgTree")
    @Query(value = "from Org p where p.parentId is null and deleted = 0 order by p.id asc")
    List<Org> findOrgTree();

    Org findByOrgCode(Integer orgCode);
}