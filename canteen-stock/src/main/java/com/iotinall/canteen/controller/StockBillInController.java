package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.inbill.StockInAcceptanceReq;
import com.iotinall.canteen.dto.inbill.StockInApplyReq;
import com.iotinall.canteen.service.StockBillInService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 入库控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_BILL_IN)
@RestController
@RequestMapping("stock/bill-in")
public class StockBillInController {
    @Resource
    private StockBillInService stockInBillService;

    @ApiOperation(value = "采购入库申请", httpMethod = "POST")
    @PostMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_IN','STOCK_IN_APPLY','APP_JXC','APP_JXC_CGRK_SQ')")
    public ResultDTO apply(@Valid @RequestBody StockInApplyReq req) {
        stockInBillService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "采购入库申请编辑", httpMethod = "PUT")
    @PutMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_IN','STOCK_IN_APPLY','APP_JXC','APP_JXC_CGRK_SQ')")
    public ResultDTO updateInApply(@Valid @RequestBody StockInApplyReq req) {
        stockInBillService.update(req);
        return ResultDTO.success();
    }

    @CrossOrigin
    @ApiOperation(value = "采购入库单个验收", httpMethod = "POST")
    @PostMapping("acceptance")
    public ResultDTO acceptance(@Valid StockInAcceptanceReq req) {
        stockInBillService.acceptance(req);
        return ResultDTO.success();
    }

    @CrossOrigin
    @ApiOperation(value = "采购入库单个验收", httpMethod = "POST")
    @PostMapping("acceptance/app")
    public ResultDTO acceptanceApp(@Valid @RequestBody StockInAcceptanceReq req) {
        stockInBillService.acceptance(req);
        return ResultDTO.success();
    }
}
