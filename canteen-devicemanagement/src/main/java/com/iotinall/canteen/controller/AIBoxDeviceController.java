package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.aibox.AIBoxAddReq;
import com.iotinall.canteen.dto.aibox.AIBoxEditReq;
import com.iotinall.canteen.service.AIBoxDeviceService;
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

/**
 * 摄像头 Controller
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
@Api(tags = SwaggerModule.MODULE_DEVICE)
@RestController
@RequestMapping("/device/ai-box")
public class AIBoxDeviceController {

    @Resource
    private AIBoxDeviceService aiBoxDeviceService;

    /**
     * 查询equ_face_device列表
     *
     * @author xin-bing
     * @date 2019-11-26 20:31:06
     */
    @ApiOperation(value = "分页列表", notes = "分页列表")
    @GetMapping
    public ResultDTO list(@RequestParam(value = "keywords", required = false) String keywords,
                          @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(aiBoxDeviceService.page(keywords, pageable));
    }

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_AIBOX_EDIT')")
    public ResultDTO create(@Valid @RequestBody AIBoxAddReq req) {
        aiBoxDeviceService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_AIBOX_EDIT')")
    public ResultDTO update(@Valid @RequestBody AIBoxEditReq req) {
        aiBoxDeviceService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_AIBOX_EDIT')")
    public ResultDTO deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        aiBoxDeviceService.batchDelete(ids);
        return ResultDTO.success();
    }
}