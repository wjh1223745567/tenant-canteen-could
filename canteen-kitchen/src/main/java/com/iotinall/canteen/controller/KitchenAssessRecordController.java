package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.assessrecord.*;
import com.iotinall.canteen.service.KitchenAssessRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 考核记录
 *
 * @author xinbing
 * @date 2020-07-02
 */
@RestController
@RequestMapping("kitchen/assess-record")
public class KitchenAssessRecordController {
    @Resource
    private KitchenAssessRecordService kitchenAssessRecordService;

    @GetMapping
    @ApiOperation(value = "考核记录列表")
    public ResultDTO<PageDTO<AssessRecordDTO>> list(AssessRecordListReq req, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenAssessRecordService.list(req, pageable));
    }

    @GetMapping(value = "list_emp")
    @ApiOperation(value = "查询考核人员")
    public ResultDTO<?> listEmp(@Valid AssessRecordEmpReq recordEmpReq){
        return ResultDTO.success(this.kitchenAssessRecordService.listEmp(recordEmpReq));
    }

    @PostMapping(value = "list_content")
    @ApiOperation(value = "查询考核内容")
    public ResultDTO listContent(@Valid @RequestBody AssessRecordContentReq contentReq){
        return ResultDTO.success(this.kitchenAssessRecordService.listContent(contentReq));
    }


    @PostMapping
    @ApiOperation(value = "添加覆盖考核记录")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ASSESS','APP_HCGL','APP_HCGL_YGKH')")
    public ResultDTO<?> add(@Validated @RequestBody AssessRecordAddReq req) {
        kitchenAssessRecordService.add(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除考核记录")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ASSESS','APP_HCGL','APP_HCGL_YGKH')")
    public ResultDTO<?> del(
            @ApiParam(value = "多个id以,分割")
            @RequestParam(value = "batch") Long[] batch) {
        return ResultDTO.success(kitchenAssessRecordService.del(batch));
    }
}
