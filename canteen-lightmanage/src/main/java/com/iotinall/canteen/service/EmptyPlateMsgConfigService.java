package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateMsgConfigAddReq;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateMsgConfigDTO;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateMsgConfigEditReq;
import com.iotinall.canteen.dto.tcpclient.FeignTcpClientDTO;
import com.iotinall.canteen.entity.EmptyPlateMsgConfig;
import com.iotinall.canteen.repository.EmptyPlateMsgConfigRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 光盘行动消息推送配置
 *
 * @author loki
 * @date 2021/7/7 11:10
 **/
@Service
public class EmptyPlateMsgConfigService {
    @Resource
    private EmptyPlateMsgConfigRepository emptyPlateMsgConfigRepository;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;
    @Resource
    private FeignTcpClientService feignTcpClientService;

    /**
     * 分页
     *
     * @author loki
     * @date 2021/7/7 11:17
     **/
    public PageDTO<EmptyPlateMsgConfigDTO> page(String keywords, Pageable page) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("name", keywords));

        Page<EmptyPlateMsgConfig> pageResult = emptyPlateMsgConfigRepository.findAll(builder.build(), page);

        return PageUtil.toPageDTO(pageResult.stream().map(this::convert).collect(Collectors.toList()),
                pageResult);
    }

    /**
     * DTO转换
     *
     * @author loki
     * @date 2021/7/7 11:36
     **/
    private EmptyPlateMsgConfigDTO convert(EmptyPlateMsgConfig config) {
        EmptyPlateMsgConfigDTO configDTO = new EmptyPlateMsgConfigDTO();
        if (null != config.getCameraId()) {
            FeignCameraDto camera = feignEquFaceDeviceService.findById(config.getCameraId());
            configDTO.setCameraId(config.getCameraId());
            configDTO.setCameraName(camera.getName());
        }

        if (null != config.getClientId()) {
            FeignTcpClientDTO feignTcpClientDTO = feignTcpClientService.findById(config.getClientId());
            configDTO.setClientId(config.getClientId());
            configDTO.setClientName(feignTcpClientDTO.getClientName());
        }
        configDTO.setId(config.getId());
        configDTO.setName(config.getName());

        return configDTO;
    }

    /**
     * 创建
     *
     * @author loki
     * @date 2021/7/7 11:19
     **/
    public void create(EmptyPlateMsgConfigAddReq req) {
        EmptyPlateMsgConfig config = this.emptyPlateMsgConfigRepository.findByCameraIdAndAndClientId(req.getCameraId(), req.getClientId());
        if (null != config) {
            throw new BizException("配置已存在");
        }

        config = new EmptyPlateMsgConfig();
        config.setCameraId(req.getCameraId());
        config.setClientId(req.getClientId());
        config.setName(req.getName());

        this.emptyPlateMsgConfigRepository.save(config);
    }

    /**
     * 编辑
     *
     * @author loki
     * @date 2021/7/7 11:21
     **/
    public void update(EmptyPlateMsgConfigEditReq req) {
        EmptyPlateMsgConfig config = this.emptyPlateMsgConfigRepository.findById(req.getId()).orElseThrow(() -> new BizException("配置不存在"));

        EmptyPlateMsgConfig config1 = this.emptyPlateMsgConfigRepository.findByCameraIdAndAndClientId(req.getCameraId(), req.getClientId());
        if (null != config1 && !config1.getId().equals(req.getId())) {
            throw new BizException("配置已存在");
        }

        config.setCameraId(req.getCameraId());
        config.setClientId(req.getClientId());
        config.setName(req.getName());

        this.emptyPlateMsgConfigRepository.save(config);
    }

    /**
     * 批量删除
     *
     * @author loki
     * @date 2021/7/7 11:22
     **/
    public void batchDelete(Long[] ids) {
        if (ids.length <= 0) {
            throw new BizException("请选择需要删除的项");
        }

        for (Long id : ids) {
            this.emptyPlateMsgConfigRepository.deleteById(id);
        }
    }
}
