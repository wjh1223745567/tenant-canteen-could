package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "canteen-devicemanagement", contextId = "equface-device")
public interface FeignEquFaceDeviceService {

    @GetMapping(value = "/device/camera/feign/findDtoById/{id}")
    FeignCameraDto findById(@PathVariable(value = "id") Long id);

    @PostMapping(value = "/device/camera/feign/findMapByIds")
    Map<Long, FeignCameraDto> findMapByIds(@RequestBody Set<Long> ids);

    @GetMapping(value = "/device/camera/feign/findByDeviceNo/{deviceNo}")
    FeignCameraDto findByDeviceNo(@PathVariable(value = "deviceNo") String deviceNo, @RequestHeader(value = "token") String token);
}
