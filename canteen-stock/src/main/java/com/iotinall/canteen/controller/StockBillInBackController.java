package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.inbill.StockInBackAcceptanceReq;
import com.iotinall.canteen.dto.inbill.StockInBackApplyReq;
import com.iotinall.canteen.service.StockBillInBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 退货控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_BILL_IN_BACK)
@RestController
@RequestMapping("stock/bill-in-back")
public class StockBillInBackController {
    @Resource
    private StockBillInBackService stockBillInBackService;

    @ApiOperation(value = "采购退货申请", httpMethod = "POST")
    @PostMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_IN_BACK','STOCK_IN_BACK_APPLY','APP_JXC','APP_JXC_CGTH_SQ')")
    public ResultDTO create(@Valid @RequestBody StockInBackApplyReq req) {
        stockBillInBackService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "采购退货申请编辑", httpMethod = "PUT")
    @PutMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_IN_BACK','STOCK_IN_BACK_APPLY','APP_JXC','APP_JXC_CGTH_SQ')")
    public ResultDTO updateInBackApply(@Valid @RequestBody StockInBackApplyReq req) {
        stockBillInBackService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "采购退货单个验收", httpMethod = "POST")
    @PostMapping("acceptance")
    public ResultDTO acceptance(@Valid StockInBackAcceptanceReq req) {
        stockBillInBackService.acceptance(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "采购退货单个验收", httpMethod = "POST")
    @PostMapping("acceptance/app")
    public ResultDTO acceptanceApp(@Valid @RequestBody StockInBackAcceptanceReq req) {
        stockBillInBackService.acceptance(req);
        return ResultDTO.success();
    }
}
