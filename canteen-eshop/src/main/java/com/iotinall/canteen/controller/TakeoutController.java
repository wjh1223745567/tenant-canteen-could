package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.sourcing.OutSourcingCondition;
import com.iotinall.canteen.service.TakeoutOrderService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 外带订单
 *
 * @author WJH
 * @date 2019/11/2214:56
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping(value = "takeouts")
public class TakeoutController {

    @Resource
    private TakeoutOrderService takeoutOrderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TAKE_OUT_ALL','TAKE_OUT_ORDER_ALL')")
    public ResultDTO findByPage(OutSourcingCondition condition, Pageable pageable) {
        return ResultDTO.success(takeoutOrderService.findByPage(condition, pageable));
    }

    /**
     * 打印汇总
     * @return
     */
    @GetMapping(value = "print_collect")
    @PreAuthorize("hasAnyRole('ADMIN','TAKE_OUT_ALL','TAKE_OUT_ORDER_ALL')")
    public ResultDTO printCollect(){
        return ResultDTO.success(this.takeoutOrderService.printCollect());
    }

    @PostMapping(value = "pickup")
    public ResultDTO pickup(@RequestParam(value = "id") Long id) {
        takeoutOrderService.pickup(id);
        return ResultDTO.success();
    }
}
