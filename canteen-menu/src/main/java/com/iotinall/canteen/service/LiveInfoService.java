package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constants.AreaType;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.equcamera.EquCameraAddReq;
import com.iotinall.canteen.dto.equcamera.EquCameraDTO;
import com.iotinall.canteen.dto.equcamera.EquCameraEditReq;
import com.iotinall.canteen.dto.equcamera.EquCameraQueryCriteria;
import com.iotinall.canteen.dto.storehouse.FeignTenantSubOrganizationDto;
import com.iotinall.canteen.entity.LiveInfo;
import com.iotinall.canteen.repository.LiveInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/2811:35
 */
@Slf4j
@Service
public class LiveInfoService {

    @Resource
    private LiveInfoRepository liveInfoRepository;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;
    @Resource
    private FeignTenantSubOrganizationService feignTenantSubOrganizationService;

    public List<EquCameraDTO> findByPage(EquCameraQueryCriteria query) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.like("name", query.getName()))
                .where(Criterion.eq("areaType", null == query.getAreaType() ? null : AreaType.findByCode(query.getAreaType())))
                .where(Criterion.eq("tenantOrgId", query.getTenantOrgId()));

        List<LiveInfo> areaManageList = liveInfoRepository.findAll(builder.build());
        Map<Long, FeignCameraDto> map = feignEquFaceDeviceService.findMapByIds(areaManageList.stream().map(LiveInfo::getDeviceId).collect(Collectors.toSet()));
        return areaManageList.stream().map(item ->
                {
                    EquCameraDTO cameraDTO = new EquCameraDTO()
                            .setId(item.getId())
                            .setName(item.getName())
                            .setDefaultImg(item.getDefaultImg())
                            //获取图片
                            .setAreaType(item.getAreaType().getCode())
                            .setAreaName(item.getAreaName())
                            .setCreateTime(item.getCreateTime())
                            .setUpdateTime(item.getUpdateTime())
                            .setRemark(item.getRemark());

                    if (map.containsKey(item.getDeviceId()) && item.getDeviceId() != null) {
                        FeignCameraDto deviceDto = map.get(item.getDeviceId());
                        cameraDTO.setImg(deviceDto != null ? deviceDto.getImage() : null)
                                .setDeviceId(deviceDto != null ? deviceDto.getId() : null);
                    }

                    if (null != item.getTenantOrgId()) {
                        FeignTenantSubOrganizationDto subOrganizationDto = feignTenantSubOrganizationService.findById(item.getTenantOrgId());
                        if (null != subOrganizationDto) {
                            cameraDTO.setTenantOrgId(subOrganizationDto.getId());
                            cameraDTO.setTenantOrgName(subOrganizationDto.getName());
                        }
                    }

                    return cameraDTO;
                }
        ).collect(Collectors.toList());

    }

    @Transactional(rollbackFor = Exception.class)
    public void addCamera(EquCameraAddReq req) {
        FeignCameraDto cameraDto = feignEquFaceDeviceService.findById(req.getDeviceId());
        if (null == cameraDto) {
            throw new BizException("摄像头不存在");
        }

        LiveInfo camera = new LiveInfo();
        camera.setName(req.getName())
                .setDeviceId(req.getDeviceId())
                .setDefaultImg(req.getDefaultImg())
                .setAreaType(AreaType.findByCode(req.getAreaType()))
                .setAreaName(cameraDto.getAreaName())
                .setTenantOrgId(cameraDto.getTenantOrgId())
                .setRemark(req.getRemark());
        this.liveInfoRepository.save(camera);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editCamera(EquCameraEditReq req) {
        LiveInfo camera = liveInfoRepository.findById(req.getId()).orElseThrow(() -> new BizException("", "记录不存在"));

        camera.setName(req.getName())
                .setDeviceId(req.getDeviceId())
                .setDefaultImg(req.getDefaultImg())
                .setAreaType(AreaType.findByCode(req.getAreaType()))
                .setRemark(req.getRemark());
        liveInfoRepository.save(camera);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleted(Long id) {
        this.liveInfoRepository.deleteById(id);
    }


    public List<LiveInfo> findByAreaType(AreaType areaType) {
        return liveInfoRepository.findByAreaType(areaType);
    }

}
