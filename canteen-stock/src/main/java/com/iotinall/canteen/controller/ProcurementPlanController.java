package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.procurementplan.InOrderReq;
import com.iotinall.canteen.dto.procurementplan.LinkReq;
import com.iotinall.canteen.service.ProcurementPlanService;
import com.iotinall.canteen.service.StockGoodsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 智慧采购
 */
@RestController
@RequestMapping(value = "procurement_plan")
public class ProcurementPlanController {

    @Resource
    private ProcurementPlanService procurementPlanService;

    @Resource
    private StockGoodsService stockGoodsService;

    /**
     * 获取当日菜谱原料
     * @param date
     * @return
     */
    @GetMapping(value = "list")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','PROCUREMENT_PLAN_LIST')")
    public ResultDTO list(@RequestParam LocalDate date){
        return ResultDTO.success(this.procurementPlanService.findProductProcurement(date));
    }

    /**
     * 创建订单
     * @return
     */
    @PostMapping(value = "create_in_order")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','PROCUREMENT_PLAN_LIST')")
    public ResultDTO createInOrder(@Valid @RequestBody List<InOrderReq> inOrderReq){
        this.procurementPlanService.createInOrder(inOrderReq);
        return ResultDTO.success();
    }

    /**
     * 根据名称获取最相近商品信息
     * @param names
     * @return
     */
    @PostMapping(value = "find_by_vector")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','PROCUREMENT_PLAN_LIST')")
    public ResultDTO findByVector(@RequestBody List<String> names){
        return ResultDTO.success(this.procurementPlanService.findCacheName(names));
    }


    /**
     * 根据ID查询用户原料
     * @param id
     * @return
     */
    @GetMapping(value = "traceability_chain/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','PROCUREMENT_PLAN_LIST')")
    public ResultDTO traceabilityChain(@PathVariable("id") Long id){
        return ResultDTO.success(this.procurementPlanService.traceabilityChain(id));
    }

    @PostMapping(value = "link")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','PROCUREMENT_PLAN_LIST')")
    public ResultDTO link(@RequestBody LinkReq req){
        return ResultDTO.success(this.procurementPlanService.link(req.getIds(), req.getDate()));
    }

}
