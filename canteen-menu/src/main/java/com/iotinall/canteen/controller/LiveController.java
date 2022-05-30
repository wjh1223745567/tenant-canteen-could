package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.AreaType;
import com.iotinall.canteen.service.LiveService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author WJH
 * @date 2019/11/2817:07
 */
@Api(tags = "餐厅实况")
@RestController
@RequestMapping(value = "live")
public class LiveController {

    @Resource
    private LiveService liveService;

    // 餐位信息
    @GetMapping(value = "seat-info/{id}")
    public ResultDTO<?> getSeatInfo(@PathVariable(value = "id")Long id){
        return ResultDTO.success(liveService.getSeatInfo(id));
    }

    // 取菜区域
    @GetMapping(value = "take-area/{id}")
    public ResultDTO<?> getMonitorArea(@PathVariable(value = "id")Long id) {
        return ResultDTO.success(liveService.getMonitorArea(id,AreaType.TAKE));
    }

    // 就餐区域
    @GetMapping(value = "eat-area/{id}")
    public ResultDTO<?> getEatArea(@PathVariable(value = "id")Long id) {
        return ResultDTO.success(liveService.getMonitorArea(id,AreaType.EAT));
    }
}
