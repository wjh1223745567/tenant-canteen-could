package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.bill.StockStatQueryReq;
import com.iotinall.canteen.dto.outbill.OrgStockOutQueryReq;
import com.iotinall.canteen.dto.stock.*;
import com.iotinall.canteen.service.StockBillStatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单据控制器
 * 采购入库，出库，退货，退库公共的操作放在此处
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_BILL_STAT)
@RestController
@RequestMapping("stock/bill/stat")
public class StockBillStatController {
    @Resource
    private StockBillStatService stockBillStatService;

    @ApiOperation(value = "库存汇总列表", notes = "库存汇总列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO stockStatistics(@ModelAttribute StockStatQueryReq req, Pageable page) {
        return ResultDTO.success(stockBillStatService.statStock(req, page));
    }

    @ApiOperation(value = "入库/出库汇总-部门领用汇总列表", notes = "入库/出库汇总-部门领用汇总列表", httpMethod = "GET")
    @GetMapping("in-out-detail")
    public ResultDTO orgStockOutStatistics(OrgStockOutQueryReq req, Pageable page) {
        return ResultDTO.success(stockBillStatService.statStockInOutDetail(req, page));
    }

    @ApiOperation(value = "食堂大屏-仓库实况", notes = "食堂大屏-仓库实况", httpMethod = "GET")
    @GetMapping("feign/getTodayStockMoney")
    public FeignTodayStockMoneyDTO getTodayStockMoney() {
        return stockBillStatService.getTodayStockMoney();
    }

    @ApiOperation(value = "食堂大屏-近30日出库、入库金额统计", notes = "食堂大屏-近30日出库、入库金额统计", httpMethod = "GET")
    @GetMapping("feign/getStockMoney30")
    public List<FeignStat30StockInOutMoney> getStockMoney30() {
        return stockBillStatService.getStockMoney30();
    }

    @ApiOperation(value = "食堂大屏-采购前5", notes = "食堂大屏-采购前5", httpMethod = "GET")
    @GetMapping("/feign/getStockInTop5")
    public List<StatStockInTop5> getStockInTop5() {
        return stockBillStatService.getStockInTop5();
    }

    @ApiOperation(value = "食堂大屏-采购前5", notes = "食堂大屏-采购前5", httpMethod = "GET")
    @GetMapping("feign/getStockWarning")
    public List<StatStockWarningDTO> getStockWarning() {
        return stockBillStatService.getStockWarning();
    }

    @ApiOperation(value = "食堂大屏-采购入库根据类型分组返回前5", notes = "食堂大屏-采购入库根据类型分组返回前5", httpMethod = "GET")
    @GetMapping("feign/getStockInTotalMoneyByType")
    public List<StockInCostDTO> getStockInTotalMoneyByType() {
        return stockBillStatService.getStockInTotalMoneyByType();
    }

    /**
     * 食堂大屏-验收图片-检测报告
     *
     * @author loki
     * @date 2021/7/13 15:49
     **/
    @ApiOperation(value = "食堂大屏-验收图片-检测报告", notes = "食堂大屏-验收图片-检测报告", httpMethod = "GET")
    @GetMapping("getBillAcceptImgAndReportImg")
    public List<StockAcceptImgAndReportImg> getBillAcceptImgAndReportImg() {
        return stockBillStatService.getBillAcceptImgAndReportImg();
    }
}
