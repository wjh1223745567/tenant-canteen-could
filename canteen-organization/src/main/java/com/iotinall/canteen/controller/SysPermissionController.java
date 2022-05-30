package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.entity.SysPermission;
import com.iotinall.canteen.service.SysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统权限 Controller
 *
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
@Api(tags = "系统权限接口")
@RestController
@RequestMapping("sys/permissions")
public class SysPermissionController {
    @Resource
    private SysPermissionService sysPermissionService;

    @ApiOperation(value = "查询系统权限", notes = "根据条件查询系统权限列表")
    @GetMapping
    public ResultDTO<List<SysPermission>> list() {
        return ResultDTO.success(sysPermissionService.listAll());
    }

    @ApiOperation(value = "根据类型查询系统权限", notes = "根据类型查询系统权限")
    @GetMapping("/{type}")
    public ResultDTO list(@PathVariable("type") Integer type) {
        return ResultDTO.success(sysPermissionService.listAll(type));
    }
}