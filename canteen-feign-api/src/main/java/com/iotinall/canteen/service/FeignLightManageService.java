package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.lightkitchen.LightKitchenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 名厨亮灶、光盘行动服务
 *
 * @author loki
 * @date 2021/7/13 14:40
 **/
@FeignClient(value = "canteen-light-manage", contextId = "light-manage")
public interface FeignLightManageService {
    /**
     * 名厨亮灶违规记录列表
     *
     * @author loki
     * @date 2021/7/13 14:41
     **/
    @GetMapping(value = "light-kitchen/getLightKitchenLatest12")
    List<LightKitchenDTO> getLightKitchenLatest12();
}
