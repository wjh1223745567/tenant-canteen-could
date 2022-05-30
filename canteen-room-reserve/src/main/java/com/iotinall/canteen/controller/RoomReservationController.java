package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.roomreservation.RoomReservationAddReq;
import com.iotinall.canteen.dto.roomreservation.RoomReservationCondition;
import com.iotinall.canteen.dto.roomreservation.RoomReservationUpdateReq;
import com.iotinall.canteen.dto.roomreservation.RoomReserveCondition;
import com.iotinall.canteen.service.RoomReservationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 包间管理controller
 *
 * @author loki
 * @date 2021/7/28 13:56
 **/
@RestController
@RequestMapping(value = "room_reservation")
public class RoomReservationController {

    @Resource
    private RoomReservationService roomReservationService;

    @ApiOperation(value = "包间列表", notes = "包间列表")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ROOM_RESERVATION_ALL','ROOM_RESERVATION_ROOM_ALL')")
    public ResultDTO<?> findList(RoomReservationCondition condition, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(this.roomReservationService.findRoomReservation(condition, pageable));
    }

    @ApiOperation(value = "添加包间", notes = "添加包间", response = RoomReservationAddReq.class)
    @PostMapping("add")
    @PreAuthorize("hasAnyRole('ADMIN','ROOM_RESERVATION_ALL','ROOM_RESERVATION_ROOM_ALL')")
    public ResultDTO<?> add(@Valid @RequestBody RoomReservationAddReq req) {
        this.roomReservationService.addRoom(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "编辑包间", notes = "编辑包间", response = RoomReservationUpdateReq.class)
    @PutMapping("update")
    @PreAuthorize("hasAnyRole('ADMIN','ROOM_RESERVATION_ALL','ROOM_RESERVATION_ROOM_ALL')")
    public ResultDTO<?> update(@Valid @RequestBody RoomReservationUpdateReq req) {
        this.roomReservationService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "删除包间", notes = "删除包间")
    @GetMapping("delete")
    @PreAuthorize("hasAnyRole('ADMIN','ROOM_RESERVATION_ALL','ROOM_RESERVATION_ROOM_ALL')")
    public ResultDTO<?> del(Long[] id) {
        return ResultDTO.success(this.roomReservationService.del(id));
    }

    @ApiOperation(value = "包间预定列表", notes = "包间预定列表")
    @GetMapping("room/reserve")
    @PreAuthorize("hasAnyRole('ADMIN','ROOM_RESERVATION_ALL','ROOM_RESERVATION_APPLY_ALL')")
    public ResultDTO<?> findRoomReserve(RoomReserveCondition condition, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(this.roomReservationService.findRoomReserve(condition, pageable));
    }
}
