package com.iotinall.canteen.service;

import com.iotinall.canteen.constants.AreaType;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.live.MonitorAreaDTO;
import com.iotinall.canteen.dto.live.SeatInfoDTO;
import com.iotinall.canteen.dto.storehouse.FeignTenantSubOrganizationDto;
import com.iotinall.canteen.entity.LiveInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bingo
 * @date 1/10/2020 20:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LiveService {
    @Resource
    private LiveInfoService equCameraService;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;
    @Resource
    private FeignTenantSubOrganizationService feignTenantSubOrganizationService;

    public SeatInfoDTO getSeatInfo(Long id) {
        SeatInfoDTO seatInfoDTO = new SeatInfoDTO();

        //食堂总餐位数
        FeignTenantSubOrganizationDto tenantSubOrg = feignTenantSubOrganizationService.findById(id);
        seatInfoDTO.setCapacity(tenantSubOrg.getCapacity());

        //食堂就餐人数
        seatInfoDTO.setDinerCount(0);
        return seatInfoDTO;
    }

    public List<MonitorAreaDTO> getMonitorArea(Long id,AreaType areaType) {
        List<LiveInfo> equCameras = equCameraService.findByAreaType(areaType);
        Map<Long, FeignCameraDto> map = feignEquFaceDeviceService.findMapByIds(equCameras.stream().map(LiveInfo::getDeviceId).collect(Collectors.toSet()));

        List<MonitorAreaDTO> monitorAreaDTOS = equCameras.stream().map(item -> {
            MonitorAreaDTO dto = new MonitorAreaDTO();
            dto.setId(item.getId());
            dto.setDefaultImg(item.getDefaultImg());
            dto.setName(item.getName());
            dto.setAreaName(item.getAreaName());
            dto.setDetectTime(item.getDetectTime());
            if (map.containsKey(item.getDeviceId()) && map.get(item.getDeviceId()) != null) {
                FeignCameraDto faceDeviceDto = map.get(item.getDeviceId());
                dto.setImg(faceDeviceDto.getImage());
            }
            return dto;
        }).collect(Collectors.toList());
        return monitorAreaDTOS;
    }
}
