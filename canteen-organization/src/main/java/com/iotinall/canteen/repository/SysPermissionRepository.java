package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.SysPermission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 系统权限 Repository
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {

    @Query(value = "select p from SysPermission p where p.pid is null order by p.sort")
    List<SysPermission> listRootPermission();

    @Query(value = "select p from SysPermission p where p.pid is null and p.permissionType = :type order by p.sort")
    List<SysPermission> listRootPermission(@Param("type") Integer type);

    Set<SysPermission> findByPermissionIn(List<String> permissions);

    @EntityGraph(value = "SysPermission.loadPermissionTree")
    @Query(value = "select p from SysPermission p where p.id is null order by p.sort")
    List<SysPermission> loadPermissionTree();
}