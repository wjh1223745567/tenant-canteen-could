package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.stock.StockInventoryApplyReq;
import com.iotinall.canteen.dto.stock.StockInventoryAuditReq;
import com.iotinall.canteen.service.StockBillInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 库存盘点控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_BILL_INVENTORY)
@RestController
@RequestMapping("stock/bill-inventory")
public class StockBillInventoryController {
    @Resource
    private StockBillInventoryService stockBillInventoryService;

    @ApiOperation(value = "库存盘点申请", httpMethod = "POST")
    @PostMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_INVENTORY','STOCK_INVENTORY_APPLY','APP_JXC','APP_JXC_KCPD_SQ')")
    public ResultDTO apply(@Valid @RequestBody StockInventoryApplyReq req) {
        stockBillInventoryService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "库存盘点申请编辑", httpMethod = "PUT")
    @PutMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_INVENTORY','STOCK_INVENTORY_APPLY','APP_JXC','APP_JXC_KCPD_SQ')")
    public ResultDTO updateInApply(@Valid @RequestBody StockInventoryApplyReq req) {
        stockBillInventoryService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "库存盘点单个验收", httpMethod = "POST")
    @PostMapping("audit")
    public ResultDTO audit(@RequestBody StockInventoryAuditReq req) {
        stockBillInventoryService.audit(req);
        return ResultDTO.success();
    }
}