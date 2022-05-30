package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.apptackout.OutSourcingCondition;
import com.iotinall.canteen.dto.tackout.TackOutPayReq;
import com.iotinall.canteen.service.OutSourcingService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 外带外购订单列表
 *
 * @author WJH
 * @date 2019/11/2315:21
 */
@Api(tags = "外带外购订单")
@RestController
@RequestMapping(value = "out_sourcing")
public class OutSourcingController {

    @Resource
    private OutSourcingService outSourcingService;

    @GetMapping
    public ResultDTO findOutSourcing(OutSourcingCondition condition) {
        Long empid = SecurityUtils.getUserId();
        return ResultDTO.success(this.outSourcingService.findByPage(condition, empid));
    }

    /**
     * 查询所有
     *
     * @param condition
     * @return
     */
    @GetMapping(value = "find_all")
    public ResultDTO findOutSourcingAll(OutSourcingCondition condition) {
        return ResultDTO.success(this.outSourcingService.findAllByPage(condition));
    }

    /**
     * 发货
     *
     * @param id
     * @return
     */
    @PostMapping(value = "pickup")
    public ResultDTO pickup(@RequestParam(value = "id") Long id) {
        this.outSourcingService.pickup(id);
        return ResultDTO.success();
    }

    @GetMapping(value = "findOutSourcingById")
    public ResultDTO findOutSourcingById(@RequestParam Long id) {
        return ResultDTO.success(this.outSourcingService.findOutSourcingById(id));
    }

    @GetMapping(value = "cancelOutSourcingById")
    public ResultDTO cancelOutSourcingById(@RequestParam Long id) {
        this.outSourcingService.cancelOutSourcingById(id);
        return ResultDTO.success();
    }

    /**
     * 网上商城下单
     * @param req
     * @return
     */
    @PostMapping(value = "tackOutWxPay")
    @GlobalTransactional(rollbackFor = Exception.class)
    public ResultDTO<?> tackOutWxPay(@Valid @RequestBody TackOutPayReq req){
        return ResultDTO.success(this.outSourcingService.tackOutWxPay(req, Boolean.FALSE));
    }

    /**
     * 微信支付外带外购立即结算
     * @return
     */
    @PostMapping(value = "tackOutWxImmediatelyPay")
    @GlobalTransactional(rollbackFor = Exception.class)
    public ResultDTO tackOutWxImmediatelyPay(@Valid @RequestBody TackOutPayReq req){
        return ResultDTO.success(this.outSourcingService.tackOutWxPay(req, Boolean.TRUE));
    }

}
