package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.bill.StockBillCertificateReq;
import com.iotinall.canteen.service.StockCertificateAndInspectionReportService;
import io.swagger.annotations.Api;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 入库上传的合格证，验收报告等
 *
 * @author loki
 * @date 2021/6/10 14:41
 **/
@Api(tags = SwaggerModule.MODULE_STOCK)
@RestController
@RequestMapping(value = "stock/bill/report")
public class StockCertificateAndInspectionReportController {
    @Resource
    StockCertificateAndInspectionReportService stockCertificateAndInspectionReportService;

    @GetMapping
    public ResultDTO imgList(@Param(value = "billNo") String billNo) {
        return ResultDTO.success(stockCertificateAndInspectionReportService.imgList(billNo));
    }

    @PostMapping
    public ResultDTO update(@Valid @RequestBody StockBillCertificateReq req) {
        stockCertificateAndInspectionReportService.update(req);
        return ResultDTO.success();
    }
}
