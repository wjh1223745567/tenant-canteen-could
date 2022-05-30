package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.outbill.StockOutBackAcceptanceReq;
import com.iotinall.canteen.dto.outbill.StockOutBackApplyReq;
import com.iotinall.canteen.dto.outbill.StockOutBackGoodsQueryReq;
import com.iotinall.canteen.service.StockBillOutBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 领用退库控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_BILL_OUT_BACK)
@RestController
@RequestMapping("stock/bill-out-back")
public class StockBillOutBackController {
    @Resource
    private StockBillOutBackService stockBillOutBackService;

    @ApiOperation(value = "领用退库申请", httpMethod = "POST")
    @PostMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_OUT_BACK','STOCK_OUT_BACK_APPLY','APP_JXC','APP_JXC_LYTK_SQ')")
    public ResultDTO outBackApply(@Valid @RequestBody StockOutBackApplyReq req) {
        stockBillOutBackService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "领用退库申请编辑", httpMethod = "PUT")
    @PutMapping("apply")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_OUT_BACK','STOCK_OUT_BACK_APPLY','APP_JXC','APP_JXC_LYTK_SQ')")
    public ResultDTO updateOutBackApply(@Valid @RequestBody StockOutBackApplyReq req) {
        stockBillOutBackService.update(req);
        return ResultDTO.success();
    }

    @CrossOrigin
    @ApiOperation(value = "领用退库单个验收", httpMethod = "POST")
    @PostMapping("acceptance")
    public ResultDTO acceptance(@Valid  StockOutBackAcceptanceReq req) {
        stockBillOutBackService.acceptance(req);
        return ResultDTO.success();
    }

    @CrossOrigin
    @ApiOperation(value = "领用退库单个验收", httpMethod = "POST")
    @PostMapping("acceptance/app")
    public ResultDTO acceptanceApp(@Valid @RequestBody StockOutBackAcceptanceReq req) {
        stockBillOutBackService.acceptance(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "领用退库查询商品", httpMethod = "GET")
    @GetMapping("goods")
    public ResultDTO outBackGoods(@ModelAttribute StockOutBackGoodsQueryReq req, Pageable page) {
        return ResultDTO.success(stockBillOutBackService.getOutBackGoods(req, page));
    }
}
