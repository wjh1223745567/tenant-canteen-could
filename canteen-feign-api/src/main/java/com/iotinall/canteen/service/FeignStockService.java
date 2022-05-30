package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.stock.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(value = "canteen-stock", contextId = "stock-prod")
public interface FeignStockService {

    String prefix = "/stock/";

    @GetMapping(value = prefix + "goods/feign/findTop10Material")
    List<FeignStockProdDto> findTop10Material(@RequestParam(value = "key") String key, @RequestParam(value = "typeId", required = false) Long typeId);

    /**
     * 食堂大屏-仓库实况
     *
     * @author loki
     * @date 2021/7/13 10:02
     **/
    @GetMapping(prefix + "bill/stat/feign/getTodayStockMoney")
    FeignTodayStockMoneyDTO getTodayStockMoney();

    /**
     * 食堂大屏-近30日出库、入库金额统计
     *
     * @author loki
     * @date 2021/7/13 11:27
     **/
    @GetMapping(prefix + "bill/stat/feign/getStockMoney30")
    List<FeignStat30StockInOutMoney> getStockMoney30();

    /**
     * 食堂大屏-采购前5
     *
     * @author loki
     * @date 2021/7/13 11:27
     **/
    @GetMapping(prefix + "bill/stat/feign/getStockInTop5")
    List<FeignStat30StockInOutMoney> getStockInTop5();

    /**
     * 食堂大屏-库存预警
     *
     * @author loki
     * @date 2021/7/13 11:27
     **/
    @GetMapping(prefix + "bill/stat/feign/getStockWarning")
    List<FeignStat30StockInOutMoney> getStockWarning();

    /**
     * 食堂大屏-采购入库根据类型分组返回前5
     *
     * @author loki
     * @date 2021/7/13 11:27
     **/
    @GetMapping(prefix + "bill/stat/feign/getStockInTotalMoneyByType")
    List<FeignStat30StockInOutMoney> getStockInTotalMoneyByType();

    /**
     * 食堂大屏-验收图片-检测报告
     *
     * @author loki
     * @date 2021/7/13 15:49
     **/
    @GetMapping(prefix + "bill/stat/feign/getBillAcceptImgAndReportImg")
    List<StockAcceptImgAndReportImg> getBillAcceptImgAndReportImg();

    /**
     * 获取商品出入库记录
     *
     * @author loki
     * @date 2021/8/10 10:19
     **/
    @GetMapping(prefix + "bill/feign/source-chain")
    List<MasterMaterialDTO> getSourceChain(@RequestParam(name = "masterMaterialIds") Set<Long> masterMaterialIds);
}
