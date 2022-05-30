package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.role.FeignRoleAddReq;
import com.iotinall.canteen.dto.sysrole.*;
import com.iotinall.canteen.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色表 Controller
 *
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("sys/roles")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 查询角色表列表
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "查询角色列表", notes = "根据条件查询角色表列表")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL','SYS_ROLE','ORG_ALL','ORG_EMP_EDIT','STOCK_FLW_MANAGE','STOCK_ALL')")
    public ResultDTO<List<SysRoleDTO>> list() {
        return ResultDTO.success(sysRoleService.listAll());
    }

    /**
     * 新增角色
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "新增", notes = "新增角色")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_EDIT')")
    public ResultDTO create(@Valid @RequestBody SysRoleAddReq req) {
        return ResultDTO.success(sysRoleService.add(req));
    }

    /**
     * 修改角色
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "修改", notes = "修改角色")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_EDIT')")
    public ResultDTO update(@Valid @RequestBody SysRoleEditReq req) {
        return ResultDTO.success(sysRoleService.update(req));
    }

    /**
     * 新增角色
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "添加租户组织默认生成对应的角色", notes = "添加租户组织默认生成对应的角色")
    @PostMapping(value = "feign/createTenantDefaultRole")
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_EDIT')")
    public ResultDTO createTenantDefaultRole(@Valid @RequestBody FeignRoleAddReq req) {
        sysRoleService.createTenantDefaultRole(req);
        return ResultDTO.success();
    }

    /**
     * 删除角色
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "删除", notes = "删除角色")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_EDIT')")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id", required = true) @PathVariable Long id) {
        return ResultDTO.success(sysRoleService.delete(id));
    }

    /**
     * 查看角色的用户
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "查看用户", notes = "查看该角色的用户")
    @GetMapping(value = "{id}/users")
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_USER')")
    public ResultDTO<List<SysRoleUserDTO>> getEmployees(@ApiParam(name = "id", value = "需要查看的角色id", required = true) @PathVariable Long id) {
        return ResultDTO.success(sysRoleService.getUsers(id));
    }

    @ApiOperation(value = "角色添加用户", notes = "配置用户")
    @PostMapping(value = "{id}/users")
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_USER_EDIT')")
    public ResultDTO settingUsers(@ApiParam(name = "id", value = "需要查看的角色id", required = true) @PathVariable Long id,
                                  @RequestBody SysRoleUserSettingReq req) {
        return ResultDTO.success(sysRoleService.settingUsers(id, req));
    }

    @ApiOperation(value = "角色移除用户", notes = "移除用户")
    @DeleteMapping(value = "{id}/users")
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_USER_EDIT')")
    public ResultDTO<?> removeRoleEmp(@ApiParam(name = "id", value = "需要查看的角色id", required = true) @PathVariable Long id,
                                      @ApiParam(name = "empId", value = "要移除的用户id") @RequestParam(value = "empId") Long empId) {
        return ResultDTO.success(sysRoleService.removeUser(id, empId));
    }

    /**
     * 查看角色的权限
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "查看权限", notes = "查看该角色的权限")
    @GetMapping(value = "{id}/permissions")
    public ResultDTO<List<String>> getPermissions(@ApiParam(name = "id", value = "需要查看的角色id", required = true) @PathVariable Long id) {
        return ResultDTO.success(sysRoleService.getPermissions(id));
    }

    /**
     * 设置角色权限
     *
     * @author xin-bing
     * @date 2019-10-26 14:20:57
     */
    @ApiOperation(value = "设置权限", notes = "给角色配置权限")
    @PostMapping(value = "{id}/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','SYS_ALL', 'SYS_ROLE_PERMISSION_EDIT')")
    public ResultDTO settingPermissions(@ApiParam(name = "id", value = "需要查看的角色id", required = true) @PathVariable Long id,
                                        @Valid @RequestBody SysPermissionSettingReq req) {
        return ResultDTO.success(sysRoleService.settingPermissions(id, req));
    }
}