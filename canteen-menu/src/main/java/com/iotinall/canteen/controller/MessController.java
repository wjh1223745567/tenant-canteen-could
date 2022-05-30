package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.messdaily.MessEditReq;
import com.iotinall.canteen.entity.Mess;
import com.iotinall.canteen.service.SysMessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 餐厅 Controller
 *
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Api(tags = SwaggerModule.MODULE_CANTEEN)
@RestController
@RequestMapping("mess")
public class MessController {

    @Resource
    private SysMessService sysMessService;

    /**
     * 餐厅详情
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53
     */
    @ApiOperation(value = "获取餐厅详情", notes = "获取餐厅详情")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL','CANTEEN_SETTING','CANTEEN_CONSUME_SETTING')")
    public ResultDTO<Mess> detail() {
        return ResultDTO.success(sysMessService.detail());
    }

    /**
     * 修改餐厅
     *
     * @author xin-bing
     * @date 2019-10-22 16:10:53
     */
    @ApiOperation(value = "修改餐厅", notes = "修改餐厅")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL','CANTEEN_SETTING')")
    public ResultDTO update(@Valid @RequestBody MessEditReq messEditReq) {
        sysMessService.update(messEditReq);
        return ResultDTO.success();
    }

}