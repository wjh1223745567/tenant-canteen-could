package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.FeignStockService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 库存统计controller
 *
 * @author loki
 * @date 2021/7/12 18:27
 **/
@RestController
@RequestMapping("statistics/stock")
public class StockStatisticsController {
    @Resource
    private FeignStockService feignStockService;

    @ApiOperation(value = "食堂大屏-仓库实况", notes = "食堂大屏-仓库实况", httpMethod = "GET")
    @GetMapping("/getTodayStockMoney")
    public ResultDTO getTodayStockMoney() {
        return ResultDTO.success(feignStockService.getTodayStockMoney());
    }

    @ApiOperation(value = "食堂大屏-近30日出库、入库金额统计", notes = "食堂大屏-近30日出库、入库金额统计", httpMethod = "GET")
    @GetMapping("/getStockMoney30")
    public ResultDTO getStockMoney30() {
        return ResultDTO.success(feignStockService.getStockMoney30());
    }

    @ApiOperation(value = "食堂大屏-采购前5", notes = "食堂大屏-采购前5", httpMethod = "GET")
    @GetMapping("/getStockInTop5")
    public ResultDTO getStockInTop5() {
        return ResultDTO.success(feignStockService.getStockInTop5());
    }

    @ApiOperation(value = "食堂大屏-采购前5", notes = "食堂大屏-采购前5", httpMethod = "GET")
    @GetMapping("/getStockWarning")
    public ResultDTO getStockWarning() {
        return ResultDTO.success(feignStockService.getStockWarning());
    }

    @ApiOperation(value = "食堂大屏-采购入库根据类型分组返回前5", notes = "食堂大屏-采购入库根据类型分组返回前5", httpMethod = "GET")
    @GetMapping("getStockInTotalMoneyByType")
    public ResultDTO getStockInTotalMoneyByType() {
        return ResultDTO.success(feignStockService.getStockInTotalMoneyByType());
    }

    @ApiOperation(value = "食堂大屏-验收图片-检测报告", notes = "食堂大屏-验收图片-检测报告", httpMethod = "GET")
    @GetMapping("getBillAcceptImgAndReportImg")
    public ResultDTO getBillAcceptImgAndReportImg() {
        return ResultDTO.success(feignStockService.getBillAcceptImgAndReportImg());
    }
}
