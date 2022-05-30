package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.HaircutMasterAddReq;
import com.iotinall.canteen.dto.HaircutMasterDTO;
import com.iotinall.canteen.dto.HaircutMasterEditReq;
import com.iotinall.canteen.dto.HaircutMasterQueryCriteria;
import com.iotinall.canteen.dto.HaircutRoomDTO;
import com.iotinall.canteen.service.HaircutMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @description:理发师控制器
 * @author: JoeLau
 * @time: 2021年06月23日 16:28:11
 */

@Api
@RestController
@RequestMapping("/haircut/master")
public class HaircutMasterController {
    @Resource
    HaircutMasterService haircutMasterService;

    @GetMapping
    @ApiOperation(value = "查询", notes = "查询所有理发师")
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER','APP_HAIRCUT_MASTER')")
    public ResultDTO page(HaircutMasterQueryCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResultDTO.success(this.haircutMasterService.page(criteria, pageable));
    }

    @GetMapping("/list/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER','APP_HAIRCUT_MASTER')")
    @ApiOperation(value = "查询", notes = "查询理发师列表", response = HaircutMasterDTO.class)
    public ResultDTO getList(@PathVariable("roomId") Long roomId) {
        return ResultDTO.success(haircutMasterService.getList(roomId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL')")
    @ApiOperation(value = "添加", notes = "添加新的理发师")
    public ResultDTO create(@Valid @RequestBody HaircutMasterAddReq addReq) {
        this.haircutMasterService.create(addReq);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL')")
    @ApiOperation(value = "编辑", notes = "编辑理发师")
    public ResultDTO update(@Valid @RequestBody HaircutMasterEditReq editReq) {
        this.haircutMasterService.update(editReq);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL')")
    @ApiOperation(value = "删除", notes = "删除理发师")
    public ResultDTO delete(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        this.haircutMasterService.delete(ids);
        return ResultDTO.success();
    }
}
