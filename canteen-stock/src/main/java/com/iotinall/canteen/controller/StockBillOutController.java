package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.outbill.StockOutAcceptanceReq;
import com.iotinall.canteen.dto.outbill.StockOutApplyReq;
import com.iotinall.canteen.service.StockBillOutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 出库控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_BILL_OUT)
@RestController
@RequestMapping("stock/bill-out")
public class StockBillOutController {
    @Resource
    private StockBillOutService stockBillOutService;

    @ApiOperation(value = "领用申请", httpMethod = "POST")
    @PostMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_OUT','STOCK_OUT_APPLY','APP_JXC','APP_JXC_LYCK')")
    public ResultDTO outApply(@Valid @RequestBody StockOutApplyReq req) {
        stockBillOutService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "领用申请编辑", httpMethod = "PUT")
    @PutMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_OUT','STOCK_OUT_APPLY','APP_JXC','APP_JXC_LYCK')")
    public ResultDTO updateOutApply(@Valid @RequestBody StockOutApplyReq req) {
        stockBillOutService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "领用出库单个验收", httpMethod = "POST")
    @PostMapping("acceptance")
    public ResultDTO acceptance(@Valid StockOutAcceptanceReq req) {
        stockBillOutService.acceptance(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "领用出库单个验收", httpMethod = "POST")
    @PostMapping("acceptance/app")
    public ResultDTO acceptanceApp(@Valid @RequestBody StockOutAcceptanceReq req) {
        stockBillOutService.acceptance(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "获取所有出库单据号", httpMethod = "GET")
    @GetMapping("/bill-no")
    public ResultDTO getOutBillNoList() {
        return ResultDTO.success(stockBillOutService.getOutBillNoList());
    }

}
