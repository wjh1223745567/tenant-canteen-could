package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.brightkitchen.KitchenBrightQueryCriteria;
import com.iotinall.canteen.service.KitchenBrightService;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 违规警报
 *
 * @author bingo
 * @date 1/4/2020 18:37
 */
@RestController
@RequestMapping(value = "kitchen/kitchen-bright")
public class KitchenBrightController {
    @Resource
    private KitchenBrightService kitchenBrightService;

    @GetMapping
    public ResultDTO getAlertList(KitchenBrightQueryCriteria criteria, @PageableDefault(sort = {"detectTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenBrightService.findPage(criteria, pageable));
    }

    @DeleteMapping
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号隔离", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenBrightService.batchDelete(ids);
        return ResultDTO.success();
    }

}
