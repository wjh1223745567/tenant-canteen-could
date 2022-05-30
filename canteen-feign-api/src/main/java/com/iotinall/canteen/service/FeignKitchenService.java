package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.employee.KitchenCookDTO;
import com.iotinall.canteen.dto.kitchen.FeignKitchenLiveInfoDTO;
import com.iotinall.canteen.dto.kitchen.KitchenMorningInspectRecordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后厨模块请求接口
 *
 * @author loki
 * @date 2021/7/13 18:31
 **/
@FeignClient(value = "canteen-kitchen", contextId = "canteen-kitchen")
public interface FeignKitchenService {
    /**
     * 查询人员晨检记录
     *
     * @author loki
     * @date 2021/7/13 19:59
     **/
    @GetMapping(value = "/kitchen/morning-inspect/feign/getEmployeeMorningInspect")
    List<KitchenMorningInspectRecordDTO> getEmployeeMorningInspect(@RequestParam(value = "empId") Long empId,
                                                                   @RequestParam(value = "date") LocalDateTime date);

    /**
     * 大屏-留样记录
     *
     * @author loki
     * @date 2021/7/13 17:48
     **/
    @GetMapping(value = "kitchen/feign/getSampleImgList")
    List<KitchenCookDTO> getSampleImgList();

    /**
     * 大屏-后厨实况
     *
     * @author loki
     * @date 2021/7/16 10:20
     **/
    @GetMapping("kitchen/stat/feign/getKitchenLiveInfo")
    FeignKitchenLiveInfoDTO getKitchenLiveInfo();
}
