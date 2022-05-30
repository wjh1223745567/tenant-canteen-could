package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.SysRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色表 Repository
 *
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
public interface SysRoleRepository extends JpaRepositoryEnhance<SysRole, Long>, JpaSpecificationExecutor<SysRole> {
    SysRole findByName(String name);

    List<SysRole> findByEmployeesId(Long id);

    @EntityGraph(value = "SysRole.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<SysRole> findByEmployeesIn(List<OrgEmployee> employees);

    /**
     * 获取用户权限
     */
    @Query(value = "select r.* from sys_role r where r.id in(select role_id from sys_emp_roles where emp_id =:empId)", nativeQuery = true)
    List<SysRole> findEmployeeRoleList(@Param("empId") Long empId);

    /**
     * 根据租户组织ID获取角色
     */
    List<SysRole> findByTenantOrgId(Long tenantOrgId);

    /**
     * 根据租户组织ID获取角色
     */
    List<SysRole> findByTenantOrgIdIn(Set<Long> ids);
}