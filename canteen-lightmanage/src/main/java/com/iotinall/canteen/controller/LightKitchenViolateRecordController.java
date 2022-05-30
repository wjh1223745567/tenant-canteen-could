package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.lightkitchen.LightKitchenDTO;
import com.iotinall.canteen.dto.lightkitchen.LightKitchenQueryCriteria;
import com.iotinall.canteen.service.LightKitchenViolateRecordService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 名厨亮灶请求处理类
 *
 * @author loki
 * @date 2021/02/05 16:22
 */
@Slf4j
@RestController
@RequestMapping(value = "light-kitchen")
public class LightKitchenViolateRecordController {
    @Resource
    private LightKitchenViolateRecordService lightKitchenViolateRecordService;

    @GetMapping
    public ResultDTO page(LightKitchenQueryCriteria criteria, @PageableDefault(sort = {"detectTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(lightKitchenViolateRecordService.page(criteria, pageable));
    }

    @DeleteMapping
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号隔离", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        lightKitchenViolateRecordService.batchDelete(ids);
        return ResultDTO.success();
    }

    @GetMapping(value = "getLightKitchenLatest12")
    public List<LightKitchenDTO> getLightKitchenLatest12() {
        return lightKitchenViolateRecordService.getLightKitchenLatest12();
    }
}
