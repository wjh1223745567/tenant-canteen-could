package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.bill.BillListDto;
import com.iotinall.canteen.dto.invest.InvestMoneyReq;
import com.iotinall.canteen.dto.organization.FeignSerMoneybagReq;
import com.iotinall.canteen.service.SerMoneybagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务页面相关接口
 * @author WJH
 * @date 2019/11/510:32
 */
@Api(tags = SwaggerModule.MODULE_PAY)
@RestController
@RequestMapping(value = "ser/moneybag")
public class SerMoneybagController {

    @Resource
    private SerMoneybagService serMoneybagService;

    @ApiOperation(value = "查询余额", notes = "余额查询接口", response = BigDecimal.class)
    @GetMapping(value = "getUserBalance")
    public ResultDTO<BigDecimal> getUserBalance(){
        Long empid = SecurityUtils.getUserId();
        return ResultDTO.success(this.serMoneybagService.getUserBalance(empid));
    }

    @ApiOperation(value = "充值", notes = "余额充值接口")
    @PostMapping(value = "addBalance")
    public ResultDTO addBalance(@Valid @RequestBody InvestMoneyReq req){
        return ResultDTO.success(this.serMoneybagService.addBalance(req));
    }

    @ApiOperation(value = "查询消费记录", notes = "根据时间查询消费记录 yyyy-MM", response = BillListDto.class)
    @GetMapping(value = "findBillList")
    public ResultDTO<CursorPageDTO<BillListDto>> findBillList(String time, @RequestParam Integer type, String cursor){
        LocalDateTime datetime = null;
        if(time != null && time.length() >= 7){
            datetime = LocalDateTime.of(Integer.parseInt(time.substring(0,4)),Integer.parseInt(time.substring(5,7)),1,0, 0);
        }
        return ResultDTO.success(this.serMoneybagService.findBillList(datetime, type, cursor));
    }

    @GetMapping(value = "feign/addBalanceResult")
    public void addBalanceResult(@RequestParam(value = "out_trade_no") String out_trade_no,@RequestParam(value = "success") boolean success){
        this.serMoneybagService.addBalanceResult(out_trade_no, success);
    }

    /**
     * 钱包支付，添加订单记录
     */
    @PostMapping(value = "feign/serMoneybagPay")
    public void serMoneybagPay(@RequestBody FeignSerMoneybagReq req){
        this.serMoneybagService.serMoneybagPay(req.getPassword(), req.getAmount());
        this.serMoneybagService.addRecord(req.getAmount(), req.getTackOutOrderId(), req.getOrderCreateTime());
    }
}
