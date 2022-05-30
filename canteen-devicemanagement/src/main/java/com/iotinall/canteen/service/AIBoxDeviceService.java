package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.aibox.AIBoxAddReq;
import com.iotinall.canteen.dto.aibox.AIBoxDTO;
import com.iotinall.canteen.dto.aibox.AIBoxEditReq;
import com.iotinall.canteen.entity.DeviceAIBox;
import com.iotinall.canteen.repository.AIBoxDeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 设备服务
 *
 * @author loki
 * @date 2021/01/09 15:57
 */
@Slf4j
@Service
public class AIBoxDeviceService {
    @Resource
    private AIBoxDeviceRepository aiBoxDeviceRepository;

    /**
     * 分页列表
     *
     * @author loki
     * @date 2021/7/20 14:36
     **/
    public Object page(String keywords, Pageable pageable) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("name", keywords),
                        Criterion.like("deviceNo", keywords));

        Page<DeviceAIBox> pageResult = this.aiBoxDeviceRepository.findAll(builder.build(), pageable);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.EMPTY_LIST, pageResult);
        }

        List<AIBoxDTO> result = new ArrayList<>();
        AIBoxDTO aiBoxDTO;
        for (DeviceAIBox aibox : pageResult.getContent()) {
            aiBoxDTO = new AIBoxDTO();
            result.add(aiBoxDTO);
            BeanUtils.copyProperties(aibox, aiBoxDTO);
        }

        return PageUtil.toPageDTO(result, pageResult);
    }

    /**
     * 添加
     *
     * @author loki
     * @date 2021/7/20 14:37
     **/
    public void create(AIBoxAddReq req) {
        DeviceAIBox aiBox = new DeviceAIBox();
        BeanUtils.copyProperties(req, aiBox);

        this.aiBoxDeviceRepository.save(aiBox);
    }

    /**
     * 编辑
     *
     * @author loki
     * @date 2021/7/20 14:37
     **/
    public void update(AIBoxEditReq req) {
        DeviceAIBox aiBox = this.aiBoxDeviceRepository.findById(req.getId()).orElseThrow(() -> new BizException("设备不存在"));
        BeanUtils.copyProperties(req, aiBox);
        this.aiBoxDeviceRepository.save(aiBox);
    }

    /**
     * 批量删除
     *
     * @author loki
     * @date 2021/7/20 14:38
     **/
    public void batchDelete(Long[] ids) {
        if (ids.length <= 0) {
            throw new BizException("请选择需要删除的设备");
        }

        for (Long id : ids) {
            this.aiBoxDeviceRepository.deleteById(id);
        }
    }
}
