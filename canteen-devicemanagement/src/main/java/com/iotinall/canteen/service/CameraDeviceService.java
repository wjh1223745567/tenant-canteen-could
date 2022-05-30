package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.camera.CameraAddReq;
import com.iotinall.canteen.dto.camera.CameraDTO;
import com.iotinall.canteen.dto.camera.CameraEditReq;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.entity.DeviceCamera;
import com.iotinall.canteen.repository.CameraRepository;
import com.iotinall.canteen.utils.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * equ_face_device ServiceImpl
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CameraDeviceService {
    @Resource
    private CameraRepository cameraRepository;
    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    public PageDTO<CameraDTO> page(String keywords, Pageable pageable) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("name", keywords),
                        Criterion.like("deviceNo", keywords));

        Page<DeviceCamera> page = cameraRepository.findAll(builder.build(), pageable);
        List<CameraDTO> list = page.getContent().stream().map(item -> {
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            return new CameraDTO()
                    .setId(item.getId())
                    .setCreateTime(item.getCreateTime())
                    .setName(item.getName())
                    .setDeviceNo(item.getDeviceNo())
                    .setImg(item.getImgUrl())
                    .setDefaultImg(item.getDefaultImg())
                    .setAreaName(item.getAreaName())
                    .setTenantOrgId(item.getTenantOrgId())
                    .setRemark(item.getRemark())
                    .setUpdateTime(item.getUpdateTime());
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * 查询列表
     *
     * @return
     */
    public List<CameraDTO> findSelect() {
        Set<Long> userTenantOrgIds = SecurityUtils.getCurrentUser().getRoleTenantOrgIds();
        Set<Long> childrenIds = feignTenantOrganizationService.findAllChildren(userTenantOrgIds);

        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.in("tenantOrgId", childrenIds));
        return this.cameraRepository.findAll(builder.build()).stream().map(item -> new CameraDTO()
                .setId(item.getId())
                .setName(item.getName())).collect(Collectors.toList());
    }

    public CameraDTO detail(Long id) {
        Optional<DeviceCamera> optional = cameraRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        DeviceCamera item = optional.get();
        FileUtil.downloadImage(item.getImgUrl());
        return new CameraDTO()
                // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
                .setId(item.getId())
                .setImg(item.getImgUrl())
                .setName(item.getName())
                .setRemark(item.getRemark())
                .setUpdateTime(item.getUpdateTime())
                .setAreaName(item.getAreaName())
                .setDefaultImg(item.getDefaultImg());
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(CameraAddReq req) {
        DeviceCamera camera = this.cameraRepository.findByDeviceNo(req.getDeviceNo());
        if (null != camera) {
            throw new BizException("编号已存在");
        }

        camera = new DeviceCamera();
        BeanUtils.copyProperties(req, camera);
        this.cameraRepository.save(camera);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(CameraEditReq req) {
        DeviceCamera camera = cameraRepository.findById(req.getId()).orElseThrow(() -> new BizException("设备不存在"));

        Long count = this.cameraRepository.countByDeviceNoAndIdNot(req.getDeviceNo(), req.getId());
        if (null != count && count > 0) {
            throw new BizException("编号已存在");
        }

        BeanUtils.copyProperties(req, camera);
        cameraRepository.save(camera);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        cameraRepository.deleteByIdIn(ids);
    }

    public FeignCameraDto findDtoById(Long id) {
        return this.cameraRepository.findById(id).map(item -> {
            FeignCameraDto feignEquFaceDeviceDto = new FeignCameraDto()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setAreaName(item.getAreaName())
                    .setImage(item.getImgUrl());
            return feignEquFaceDeviceDto;
        }).orElse(null);
    }

    public Map<Long, FeignCameraDto> findMapByIds(Set<Long> ids) {
        Map<Long, FeignCameraDto> map = new HashMap<>();
        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        List<FeignCameraDto> list = this.cameraRepository.findAllById(ids).stream().map(item -> {
            FeignCameraDto feignEquFaceDeviceDto = new FeignCameraDto()
                    .setId(item.getId())
                    .setImage(item.getImgUrl())
                    .setDeviceNo(item.getDeviceNo());
            return feignEquFaceDeviceDto;
        }).collect(Collectors.toList());

        for (FeignCameraDto feignEquFaceDeviceDto : list) {
            map.put(feignEquFaceDeviceDto.getId(), feignEquFaceDeviceDto);
        }

        return map;
    }

    /**
     * 根据设备编号查询设备
     *
     * @author loki
     * @date 2021/7/7 15:24
     **/
    public FeignCameraDto findByDeviceNo(String deviceNo) {
        DeviceCamera camera = this.cameraRepository.findByDeviceNo(deviceNo);
        if (null != camera) {
            FeignCameraDto cameraDto = new FeignCameraDto();
            cameraDto.setDeviceNo(deviceNo);
            cameraDto.setTenantOrgId(camera.getTenantOrgId());
            cameraDto.setName(camera.getName());
            cameraDto.setId(camera.getId());
            return cameraDto;
        }

        return null;
    }
}