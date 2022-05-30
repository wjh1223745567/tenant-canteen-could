package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.kitchen.KitchenMorningInspectRecordDTO;
import com.iotinall.canteen.dto.morninginspect.MorningInspectAddReq;
import com.iotinall.canteen.dto.morninginspect.MorningInspectDTO;
import com.iotinall.canteen.dto.morninginspect.MorningInspectEditReq;
import com.iotinall.canteen.dto.morninginspect.MorningInspectListReq;
import com.iotinall.canteen.entity.KitchenMorningInspectRecord;
import com.iotinall.canteen.service.KitchenMorningInspectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 晨检管理
 *
 * @author xinbing
 * @date 2020-07-02
 */
@RestController
@RequestMapping("kitchen/morning-inspect")
public class KitchenMorningInspectController {
    @Resource
    private KitchenMorningInspectService kitchenMorningInspectService;

    @GetMapping
    @ApiOperation(value = "晨检记录列表")
    public ResultDTO<PageDTO<MorningInspectDTO>> list(MorningInspectListReq req, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenMorningInspectService.list(req, pageable));
    }

    @GetMapping(value = "morning")
    @ApiOperation(value = "晨检记录列表")
    public ResultDTO<?> morningList(AssessRecordContentReq contentReq) {
        return ResultDTO.success(kitchenMorningInspectService.morningList(contentReq));
    }

    @PostMapping
    @ApiOperation(value = "添加晨检记录")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_MORNING_INSPECT','APP_HCGL','APP_HCGL_CJGL','APP_HCGL_CJGL_AED')")
    public ResultDTO<?> add(@Validated @RequestBody MorningInspectAddReq req) {
        kitchenMorningInspectService.add(req);
        return ResultDTO.success();
    }

    @PutMapping
    @ApiOperation(value = "修改晨检记录")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_MORNING_INSPECT','APP_HCGL','APP_HCGL_CJGL','APP_HCGL_CJGL_AED')")
    public ResultDTO<?> edit(@Validated @RequestBody MorningInspectEditReq req) {
        kitchenMorningInspectService.edit(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除晨检记录")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_MORNING_INSPECT','APP_HCGL','APP_HCGL_CJGL','APP_HCGL_CJGL_AED')")
    public ResultDTO<?> del(
            @ApiParam(value = "多个id以,分割")
            @RequestParam(value = "batch") Long[] batch) {
        kitchenMorningInspectService.del(batch);
        return ResultDTO.success();
    }

    @GetMapping(value = "feign/getEmployeeMorningInspect")
    @ApiOperation(value = "查询用户晨检记录")
    public List<KitchenMorningInspectRecordDTO> getEmployeeMorningInspect(@RequestParam(value = "empId") Long empId, @RequestParam(value = "date") LocalDateTime date) {
        return kitchenMorningInspectService.getEmployeeMorningInspect(empId, date);
    }
}
