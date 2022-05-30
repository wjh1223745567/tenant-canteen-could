package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.roomreservation.MyReserveCancelReq;
import com.iotinall.canteen.dto.roomreservation.MyRoomReserveUpdateReq;
import com.iotinall.canteen.dto.roomreservation.ReserveCancelReq;
import com.iotinall.canteen.dto.roomreservationreserved.RoomDetailDto;
import com.iotinall.canteen.dto.roomreservationreserved.RoomReservedAddReq;
import com.iotinall.canteen.dto.roomreservationreserved.RoomReservedCondition;
import com.iotinall.canteen.dto.roomreservationreserved.RoomReservedDto;
import com.iotinall.canteen.service.RoomReservationReservedService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author HJJ
 * @date 2021/5/12 17:35
 **/
@RestController
@RequestMapping(value = "room_reserved")
public class RoomReservedController {

    @Resource
    public RoomReservationReservedService reservedService;

    @GetMapping("home/page")
    @ApiOperation(value = "包间预定-首页", notes = "包间预定-首页", response = RoomReservedDto.class)
    public ResultDTO<?> list(RoomReservedCondition condition) {
        return ResultDTO.success(this.reservedService.findAllReserved(condition));
    }

    @GetMapping("home/detail/{roomId}")
    @ApiOperation(value = "包间预定-订房", notes = "包间预定-订房", response = RoomDetailDto.class)
    public ResultDTO<?> detail(@PathVariable("roomId") Long roomId, @RequestParam(value = "date") LocalDate date) {
        return ResultDTO.success(this.reservedService.roomDetail(roomId, date));
    }

    @PostMapping("add/reserve")
    @ApiOperation(value = "订房信息", notes = "订房信息", response = RoomReservedAddReq.class)
    public ResultDTO<?> create(@Valid @RequestBody RoomReservedAddReq req) {
        return ResultDTO.success(this.reservedService.addReserve(req));
    }

    @PutMapping("update/reserve/detail")
    @ApiOperation(value = "重新提交", notes = "重新提交", response = MyRoomReserveUpdateReq.class)
    public ResultDTO<?> update(@Valid @RequestBody MyRoomReserveUpdateReq req) {
        this.reservedService.updateReserve(req);
        return ResultDTO.success();
    }

    @PutMapping("update/reserve/type/cancel")
    @ApiOperation(value = "4：取消", notes = "4：取消")
    public ResultDTO<?> cancel(@Valid @RequestBody MyReserveCancelReq req) {
        this.reservedService.updateReserveTypeCancel(req);
        return ResultDTO.success();
    }

    @GetMapping("home/list/my/reserve")
    @ApiOperation(value = "包间预定-我的预定-申请人", notes = "包间预定-我的预定-申请人")
    public ResultDTO<?> getMyReserved(Integer type, String key, Long cursor) {
        return ResultDTO.success(this.reservedService.findMyRoomReserve(cursor, type, key));
    }

    @GetMapping("home/list/my/reserve/detail/{reserveId}")
    @ApiOperation(value = "包间预定-我的预定-申请人-详情", notes = "包间预定-我的预定-申请人-详情")
    public ResultDTO<?> getMyReservedDetail(@PathVariable("reserveId") Long reserveId) {
        return ResultDTO.success(this.reservedService.myRoomReserveDetail(reserveId));
    }
}
