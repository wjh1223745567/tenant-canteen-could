package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.HaircutRoomAddReq;
import com.iotinall.canteen.dto.HaircutRoomDTO;
import com.iotinall.canteen.dto.HaircutRoomEditReq;
import com.iotinall.canteen.dto.HaircutRoomQueryReq;
import com.iotinall.canteen.service.HaircutRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * @description:理发室控制器
 * @author: JoeLau
 * @time: 2021年06月23日 16:22:34
 */

@Api
@RestController
@RequestMapping("/haircut/room")
public class HaircutRoomController {
    @Resource
    HaircutRoomService haircutRoomService;

    @GetMapping
    @ApiOperation(value = "查询", notes = "查询理发室分页", response = HaircutRoomDTO.class)
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER','APP_HAIRCUT_MASTER')")
    public ResultDTO page(HaircutRoomQueryReq criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResultDTO.success(haircutRoomService.page(criteria, pageable));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER','APP_HAIRCUT_MASTER')")
    @ApiOperation(value = "查询", notes = "查询理发室列表", response = HaircutRoomDTO.class)
    public ResultDTO getList() {
        return ResultDTO.success(haircutRoomService.getList());
    }

    @PostMapping("/app")
    @ApiOperation(value = "小程序查询", notes = "查询所有理发店")
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER','APP_HAIRCUT_MASTER')")
    public ResultDTO findAll(@Valid @RequestBody HaircutRoomQueryReq req) {
        return ResultDTO.success(haircutRoomService.findAll(req));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL')")
    @ApiOperation(value = "添加", notes = "添加新的理发室")
    public ResultDTO create(@Valid @RequestBody HaircutRoomAddReq req) {
        this.haircutRoomService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL')")
    @ApiOperation(value = "编辑", notes = "编辑理发室")
    public ResultDTO update(@Valid @RequestBody HaircutRoomEditReq req) {
        this.haircutRoomService.update(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL')")
    @ApiOperation(value = "删除", notes = "删除理发室")
    public ResultDTO delete(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch")  Long[] ids) {
        this.haircutRoomService.delete(ids);
        return ResultDTO.success();
    }

//    @DeleteMapping("/batch_delete/{id}")
//    @ApiOperation(value = "批量删除",notes = "批量删除理发室")
//    public ResultDTO batchDelete(@PathVariable("id") String id) {
//        this.haircutRoomService.batchDelete(id);
//        return ResultDTO.success();
//    }
}
