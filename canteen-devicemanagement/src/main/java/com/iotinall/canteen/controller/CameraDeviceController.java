package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.camera.CameraAddReq;
import com.iotinall.canteen.dto.camera.CameraDTO;
import com.iotinall.canteen.dto.camera.CameraEditReq;
import com.iotinall.canteen.service.CameraDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 摄像头 Controller
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
@Api(tags = SwaggerModule.MODULE_DEVICE)
@RestController
@RequestMapping("/device/camera")
public class CameraDeviceController {

    @Resource
    private CameraDeviceService cameraService;

    /**
     * 查询equ_face_device列表
     *
     * @author xin-bing
     * @date 2019-11-26 20:31:06
     */
    @ApiOperation(value = "摄像头列表", notes = "摄像头列表")
    @GetMapping
    public ResultDTO<PageDTO<CameraDTO>> list(@RequestParam(value = "keywords", required = false) String keywords,
                                              @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(cameraService.page(keywords, pageable));
    }

    @ApiOperation(value = "所有摄像头", notes = "所有摄像头")
    @GetMapping(value = "all")
    public ResultDTO<List<CameraDTO>> findSelect() {
        return ResultDTO.success(cameraService.findSelect());
    }

    /**
     * equ_face_device详情
     *
     * @author xin-bing
     * @date 2019-11-26 20:31:06
     */
    @ApiOperation(value = "详情", notes = "详情")
    @GetMapping(value = "/{id}")
    public ResultDTO<CameraDTO> detail(@ApiParam(name = "id", value = "id", required = true) @PathVariable Long id) {
        return ResultDTO.success(cameraService.detail(id));
    }

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_CAMERA_EDIT')")
    public ResultDTO create(@Valid @RequestBody CameraAddReq req) {
        cameraService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_CAMERA_EDIT')")
    public ResultDTO update(@Valid @RequestBody CameraEditReq req) {
        cameraService.update(req);
        return ResultDTO.success();
    }

    /**
     * 批量删除equ_face_device
     *
     * @author xin-bing
     * @date 2019-11-26 20:31:06
     */
    @ApiOperation(value = "批量删除e")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_CAMERA_EDIT')")
    public ResultDTO deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        cameraService.batchDelete(ids);
        return ResultDTO.success();
    }

    @GetMapping(value = "feign/findDtoById/{id}")
    public FeignCameraDto findDtoById(@PathVariable(value = "id") Long id) {
        return cameraService.findDtoById(id);
    }

    @PostMapping(value = "feign/findMapByIds")
    public Map<Long, FeignCameraDto> findMapByIds(@RequestBody Set<Long> ids) {
        return cameraService.findMapByIds(ids);
    }

    @GetMapping(value = "feign/findByDeviceNo/{deviceNo}")
    public FeignCameraDto findByDeviceNo(@PathVariable(value = "deviceNo") String deviceNo) {
        return cameraService.findByDeviceNo(deviceNo);
    }
}