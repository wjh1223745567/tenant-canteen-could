package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.stock.FeignProcurementDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * 智慧采购
 */
@FeignClient(value = "canteen-menu", contextId = "procurement-plan")
public interface FeignProcurementPlanService {

    /**
     * 获取菜单
     * @param date
     * @return
     */
    @GetMapping(value = "/mess/daily-menus/feign/findProductProcurement")
    FeignProcurementDto findProductProcurement(@RequestParam(value = "date") LocalDate date);

    /**
     * 获取单个菜谱信息
     * @param id
     * @return
     */
    @GetMapping(value = "/mess/products/feign/traceabilityChain")
    FeignProcurementDto.MenuProd traceabilityChain(@RequestParam(value = "id") Long id);

}
