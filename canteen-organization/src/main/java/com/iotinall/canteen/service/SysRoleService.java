package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.role.FeignRoleAddReq;
import com.iotinall.canteen.dto.sysrole.*;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.SysPermission;
import com.iotinall.canteen.entity.SysRole;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.repository.SysPermissionRepository;
import com.iotinall.canteen.repository.SysRoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色表 ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysRoleService {
    @Resource
    private SysRoleRepository sysRoleRepository;
    @Resource
    private SysPermissionRepository sysPermissionRepository;
    @Resource
    private OrgEmployeeRepository orgEmployeeRepository;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    public List<SysRoleDTO> listAll() {
        SpecificationBuilder specificationBuilder = SpecificationBuilder.builder();
        if (!SecurityUtils.getCurrentUser().isSupperAdmin()) {
            Set<Long> userTenantOrgIds = SecurityUtils.getCurrentUser().getRoleTenantOrgIds();
            Set<Long> childrenIds = feignTenantOrganizationService.findAllChildren(userTenantOrgIds);
            Set<Long> all = new HashSet<>();
            all.addAll(userTenantOrgIds);
            all.addAll(childrenIds);
            specificationBuilder.where(
                    Criterion.in("tenantOrgId", all)
            );
            if (all.isEmpty()) {
                return Collections.emptyList();
            }
        }
        List<SysRole> list = sysRoleRepository.findAll(specificationBuilder.build(), Sort.by(Sort.Direction.ASC, "createTime"));
        List<SysRoleDTO> collect = list.stream().map((item) -> {
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, sysRoleDTO);
            return sysRoleDTO;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 查询角色根据人员ID
     */
    public List<Long> findByEmpId(Long empId) {
        Specification<SysRole> specification = SpecificationBuilder.builder()
                .fetch("employees")
                .where(Criterion.eq("employees.id", empId))
                .build();
        return this.sysRoleRepository.findAll(specification).stream().map(SysRole::getId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public Object add(SysRoleAddReq req) {
        SysRole sysRole = sysRoleRepository.findByName(req.getName());
        if (sysRole != null) {
            throw new BizException("", "角色名已存在");
        }
        sysRole = new SysRole();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, sysRole);
        sysRole.setCreateTime(LocalDateTime.now());
        return sysRoleRepository.save(sysRole);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object update(SysRoleEditReq req) {
        Optional<SysRole> optional = sysRoleRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("", "记录不存在");
        }
        SysRole sysRole = optional.get();
        if (!sysRole.getName().equals(req.getName())) {
            SysRole byName = sysRoleRepository.findByName(req.getName());
            if (byName != null) {
                throw new BizException("", "角色名已存在");
            }
        }
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, sysRole);
        return sysRoleRepository.save(sysRole);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object delete(Long id) {
        Optional<SysRole> optional = sysRoleRepository.findById(id);
        if (optional.isPresent()) {
            sysRoleRepository.deleteById(id);
        }

        return optional.get();
    }

    public List<SysRoleUserDTO> getUsers(Long id) {
        Optional<SysRole> byId = sysRoleRepository.findById(id);
        if (!byId.isPresent()) {
            throw new BizException("", "查看的角色不存在");
        }

        List<SysRoleUserDTO> collect = byId.get().getEmployees().stream().map(item -> {
            SysRoleUserDTO userDTO = new SysRoleUserDTO();
            userDTO.setId(item.getId());
            userDTO.setMobile(item.getMobile());
            userDTO.setName(item.getName());
            userDTO.setOrgName(item.getOrg() != null ? item.getOrg().getName() : null);
            return userDTO;
        }).collect(Collectors.toList());
        return collect;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object settingUsers(Long roleId, SysRoleUserSettingReq req) {
        SysRole role = sysRoleRepository.findById(roleId).orElseThrow(() -> new BizException("", "角色不存在"));

        List<OrgEmployee> employeeList = orgEmployeeRepository.findAllById(req.getEmpIds());
        if (!CollectionUtils.isEmpty(employeeList)) {
            for (OrgEmployee employee : employeeList) {
                employee.getRoles().add(role);
            }
        }

        this.orgEmployeeRepository.saveAll(employeeList);

        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object removeUser(Long roleId, Long empId) {
        SysRole role = sysRoleRepository.findById(roleId).orElseThrow(() -> new BizException("", "角色不存在"));

        OrgEmployee employee = this.orgEmployeeRepository.findById(empId).orElseThrow(() -> new BizException("", "用户不存在"));

        if (CollectionUtils.isEmpty(employee.getRoles())) {
            return role;
        }

        Set<SysRole> leftRoles = employee.getRoles().stream().filter(item -> !Objects.equals(item.getId(), roleId)).collect(Collectors.toSet());
        employee.setRoles(leftRoles);
        this.orgEmployeeRepository.save(employee);

        return role;
    }

    public List<String> getPermissions(Long roleId) {
        Optional<SysRole> byId = sysRoleRepository.findById(roleId);
        if (!byId.isPresent()) {
            throw new BizException("", "要查看的角色不存在");
        }

        Set<SysPermission> permissions = byId.get().getPermissions();
        List<String> collect = permissions.stream().map(SysPermission::getPermission).collect(Collectors.toList());
        return collect;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object settingPermissions(Long id, SysPermissionSettingReq req) {
        SysRole role = sysRoleRepository.findById(id).orElseThrow(() -> new BizException("", "角色不存在"));

        Set<SysPermission> permission = this.sysPermissionRepository.findByPermissionIn(req.getPermissions());

        if (!CollectionUtils.isEmpty(role.getPermissions())) {
            Set<SysPermission> keepPermission = role.getPermissions().stream().filter(item -> item.getPermissionType() != req.getType().intValue()).collect(Collectors.toSet());

            if (CollectionUtils.isEmpty(permission)) {
                permission = keepPermission;
            } else {
                permission.addAll(keepPermission);
            }
        }

        role.setPermissions(permission);
        return sysRoleRepository.save(role);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createTenantDefaultRole(FeignRoleAddReq req) {
        List<SysRole> roleList = this.sysRoleRepository.findByTenantOrgId(req.getTenantOrgId());
        if (CollectionUtils.isEmpty(roleList)) {
            SysRole role = new SysRole();
            role.setName(req.getName());
            role.setTenantOrgId(req.getTenantOrgId());
            this.sysRoleRepository.save(role);
        }
    }
}