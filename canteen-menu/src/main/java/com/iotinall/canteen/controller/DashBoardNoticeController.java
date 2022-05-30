package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.notice.DashboardNoticeAdd;
import com.iotinall.canteen.dto.notice.DashboardNoticeEdit;
import com.iotinall.canteen.dto.notice.FeignDashboardNoticeDTO;
import com.iotinall.canteen.service.DashBoardNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 看板通知消息
 */
@Api(tags = "看板通知消息接口")
@RestController
@RequestMapping(value = "dashboardNotice")
public class DashBoardNoticeController {

    @Resource
    private DashBoardNoticeService dashBoardNoticeService;

    @GetMapping
    @ApiOperation(value = "查看通知消息")
    public ResultDTO list() {
        return ResultDTO.success(this.dashBoardNoticeService.list());
    }

    @PostMapping
    @ApiOperation(value = "添加通知消息")
    public ResultDTO add(@Validated @RequestBody DashboardNoticeAdd add) {
        return ResultDTO.success(this.dashBoardNoticeService.add(add));
    }

    @PutMapping
    @ApiOperation(value = "编辑通知消息")
    public ResultDTO edit(@Validated @RequestBody DashboardNoticeEdit edit) {
        return ResultDTO.success(this.dashBoardNoticeService.edit(edit));
    }

    @DeleteMapping
    @ApiOperation(value = "删除通知消息")
    public ResultDTO delete(@ApiParam(value = "多个id以,分割") @RequestParam(value = "ids") Long[] ids) {
        return ResultDTO.success(this.dashBoardNoticeService.delete(ids));
    }

    @GetMapping(value = "/feign/getDashboardNoticeList")
    public List<FeignDashboardNoticeDTO> getDashboardNoticeList() {
        return this.dashBoardNoticeService.list();
    }
}
