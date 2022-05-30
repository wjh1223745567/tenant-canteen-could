package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.bill.StockAuditReq;
import com.iotinall.canteen.dto.bill.StockBillDetailQueryReq;
import com.iotinall.canteen.dto.bill.StockBillQueryReq;
import com.iotinall.canteen.dto.bill.StockMultiAcceptanceReq;
import com.iotinall.canteen.dto.stock.MasterMaterialDTO;
import com.iotinall.canteen.service.StockBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 单据控制器
 * 采购入库，出库，退货，退库公共的操作放在此处
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_BILL_IN_BACK)
@RestController
@RequestMapping("stock/bill")
public class StockBillController {
    @Resource
    private StockBillService stockBillService;

    @ApiOperation(value = "采购入库，领用出库，采购退货，领用退库 申请单据 分页列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO page(@ModelAttribute StockBillQueryReq req,
                          @PageableDefault(sort = {"billDate", "createTime"}, direction = Sort.Direction.DESC) Pageable page) {
        return ResultDTO.success(stockBillService.page(req, page));
    }

    @ApiOperation(value = "根据入库单号获取单据详情", httpMethod = "GET")
    @GetMapping("/detail")
    public ResultDTO detail(@RequestParam("billNo") String billNo,
                            @RequestParam(required = false) Long goodsId) {
        return ResultDTO.success(stockBillService.detail(billNo, goodsId));
    }

    @ApiOperation(value = "根据入库单号获取单据操作日志", httpMethod = "GET")
    @GetMapping("/log/{billNo}")
    public ResultDTO log(@PathVariable("billNo") String billNo) {
        return ResultDTO.success(stockBillService.log(billNo));
    }

    @ApiOperation(value = "审批（采购入库，采购退货，领用申请，领用退库）", httpMethod = "POST")
    @PostMapping("audit")
    public ResultDTO audit(@Valid @RequestBody StockAuditReq req) {
        stockBillService.audit(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "取消（采购入库，采购退货，领用申请，领用退库）", httpMethod = "POST")
    @PostMapping("cancel/{billNo}")
    public ResultDTO cancel(@Valid @PathVariable String billNo) {
        stockBillService.cancel(billNo);
        return ResultDTO.success();
    }

    @ApiOperation(value = "验收（采购入库，采购退货，领用申请，领用退库）", httpMethod = "POST")
    @PostMapping("acceptance/{billNo}")
    public ResultDTO acceptance(@Valid @PathVariable("billNo") String billNo) {
        stockBillService.acceptance(billNo);
        return ResultDTO.success();
    }

    @ApiOperation(value = "小程序验收图片", tags = "小程序验收图片", httpMethod = "POST")
    @PostMapping("img")
    public ResultDTO uploadImg(@RequestParam("file") MultipartFile file,
                               @RequestParam("billNo") String billNo,
                               @RequestParam("goodsId") Long goodsId) {
        stockBillService.uploadImg(file, billNo, goodsId, false);
        return ResultDTO.success();
    }

    @ApiOperation(value = "（采购入库，采购退货，领用申请，领用退库）明细", httpMethod = "GET")
    @GetMapping("detail-list")
    public ResultDTO detail(StockBillDetailQueryReq req, Pageable page) {
        return ResultDTO.success(stockBillService.detailList(req, page));
    }

    @ApiOperation(value = "一键验收", tags = "一键验收", httpMethod = "POST")
    @PostMapping("/multi-acceptance")
    public ResultDTO multiAcceptance(@RequestBody StockMultiAcceptanceReq req) {
        stockBillService.multiAcceptance(req);
        return ResultDTO.success();
    }

    /**
     * 获取商品出入库记录
     *
     * @author loki
     * @date 2021/8/10 10:19
     **/
    @GetMapping("feign/source-chain")
    public List<MasterMaterialDTO> getSourceChain(@RequestParam(name = "masterMaterialIds") Set<Long> masterMaterialIds) {
        return stockBillService.getSourceChain(masterMaterialIds);
    }
}
