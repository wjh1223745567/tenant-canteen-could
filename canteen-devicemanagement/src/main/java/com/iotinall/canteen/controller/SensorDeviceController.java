package com.iotinall.canteen.controller;


import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.sensor.SensorAddReq;
import com.iotinall.canteen.dto.sensor.SensorDTO;
import com.iotinall.canteen.dto.sensor.updata.DevicePayload;
import com.iotinall.canteen.service.SensorDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Set;

/**
 * 传感器持久化类
 *
 * @author loki
 * @date 2020/10/10 14:17
 */
@Api(tags = SwaggerModule.MODULE_SENSOR)
@Slf4j
@RestController
@RequestMapping("/device/sensor")
public class SensorDeviceController {
    @Resource
    private SensorDeviceService sensorService;

    @ApiOperation(value = "传感器分页列表", notes = "传感器分页列表", httpMethod = "GET", response = SensorDTO.class)
    @GetMapping
    public ResultDTO page(@RequestParam(value = "keywords", required = false) String keywords,
                          @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(sensorService.page(keywords,pageable));
    }

    @ApiOperation(value = "新增传感器", notes = "新增传感器", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('CANTEEN_DEVICE','DEVICE_LORA_SENSOR_EDIT')")
    public ResultDTO create(@Valid @RequestBody SensorAddReq req) {
        return ResultDTO.success(sensorService.create(req));
    }

    @ApiOperation(value = "修改传感器", notes = "修改传感器", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('CANTEEN_DEVICE','DEVICE_LORA_SENSOR_EDIT')")
    public ResultDTO update(@Valid @RequestBody SensorAddReq req) {
        return ResultDTO.success(sensorService.update(req));
    }

    @ApiOperation(value = "批量删除传感器", notes = "批量删除传感器", httpMethod = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('CANTEEN_DEVICE','DEVICE_LORA_SENSOR_EDIT')")
    public ResultDTO batchDelete(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Set<Long> ids) {
        return ResultDTO.success(sensorService.batchDelete(ids));
    }

    @ApiOperation(value = "获取环境数据", notes = "根据类型获取环境数据", httpMethod = "GET")
    @GetMapping("data/type/{type}")
    public ResultDTO getSensorByType(@PathVariable(value = "type") Integer type) {
        return ResultDTO.success(sensorService.getInfo(type));
    }

    @ApiOperation(value = "获取环境数据", notes = "根据类型获取环境数据", httpMethod = "GET")
    @GetMapping("data/{id}")
    public ResultDTO getSensorById(@PathVariable(value = "id") Long id) {
        return ResultDTO.success(sensorService.getInfoById(id));
    }

    /**
     * 设备上报数据接口
     *
     * @author loki
     * @date 2021/7/9 17:06
     **/
    @PostMapping("event")
    public void event(@RequestBody DevicePayload data, @RequestParam String event) {
        this.sensorService.event(data, event);
    }
}
