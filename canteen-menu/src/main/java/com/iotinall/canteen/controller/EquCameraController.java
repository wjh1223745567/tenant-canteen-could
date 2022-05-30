package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.equcamera.EquCameraAddReq;
import com.iotinall.canteen.dto.equcamera.EquCameraEditReq;
import com.iotinall.canteen.dto.equcamera.EquCameraQueryCriteria;
import com.iotinall.canteen.service.LiveInfoService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 取菜区管理
 *
 * @author WJH
 * @date 2019/11/2811:47
 */
@Api(tags = SwaggerModule.MODULE_DEVICE)
@RestController
@RequestMapping(value = "equ-cameras")
public class EquCameraController {

    @Resource
    private LiveInfoService equCameraService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL', 'CANTEEN_AREA')")
    public ResultDTO listCameras(EquCameraQueryCriteria criteria) {
        return ResultDTO.success(equCameraService.findByPage(criteria));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL', 'CANTEEN_AREA_EDIT')")
    public ResultDTO addCamera(@Valid @RequestBody EquCameraAddReq req) {
        equCameraService.addCamera(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL', 'CANTEEN_AREA_EDIT')")
    public ResultDTO editCamera(@Valid @RequestBody EquCameraEditReq edit) {
        equCameraService.editCamera(edit);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL', 'CANTEEN_AREA_EDIT')")
    public ResultDTO deleteCamera(Long id) {
        equCameraService.deleted(id);
        return ResultDTO.success();
    }
}
