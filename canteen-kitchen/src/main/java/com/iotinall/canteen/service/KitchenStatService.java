package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.employee.KitchenCookDTO;
import com.iotinall.canteen.dto.kitchen.FeignKitchenLiveInfoDTO;
import com.iotinall.canteen.repository.KitchenBrightRepository;
import com.iotinall.canteen.repository.KitchenMorningInspectRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 后厨统计service
 *
 * @author loki
 * @date 2021/7/16 10:54
 **/
@Service
public class KitchenStatService {
    @Resource
    private KitchenMorningInspectRepository kitchenMorningInspectRepository;
    @Resource
    private KitchenBrightRepository kitchenBrightRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 大屏，统计餐厅实况
     *
     * @author loki
     * @date 2021/7/16 10:58
     **/
    public FeignKitchenLiveInfoDTO getKitchenLiveInfo() {
        FeignKitchenLiveInfoDTO kitchenLiveInfoDTO = new FeignKitchenLiveInfoDTO();

        //获取后厨人数
        List<KitchenCookDTO> cookList = feignEmployeeService.getKitchenCookList();
        if (!CollectionUtils.isEmpty(cookList)) {
            //晨检率
            Long checkNum = kitchenMorningInspectRepository.statMorningInspectByDate(LocalDate.now());
            kitchenLiveInfoDTO.setMorningCheckRate(new BigDecimal(checkNum / cookList.size()).setScale(2));
        }

        //违规次数
        Integer violationNumber = kitchenBrightRepository.countViolationNumber(LocalDate.now());
        kitchenLiveInfoDTO.setViolationNum(violationNumber);
        return kitchenLiveInfoDTO;
    }
}
