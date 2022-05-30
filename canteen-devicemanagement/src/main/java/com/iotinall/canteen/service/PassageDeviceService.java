package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.passage.PassageAddReq;
import com.iotinall.canteen.dto.passage.PassageDTO;
import com.iotinall.canteen.dto.passage.PassageEditReq;
import com.iotinall.canteen.entity.DevicePassage;
import com.iotinall.canteen.repository.PassageDeviceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通行设备
 *
 * @author loki
 * @date 2021/7/9 10:51
 **/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PassageDeviceService {
    @Resource
    private PassageDeviceRepository passageDeviceRepository;

    public Object page(String keywords, Pageable pageable) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("name", keywords),
                        Criterion.like("deviceNo", keywords));

        Page<DevicePassage> pageResult = this.passageDeviceRepository.findAll(builder.build(), pageable);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.EMPTY_LIST, pageResult);
        }


        List<DevicePassage> passageList = pageResult.getContent();
        List<PassageDTO> passageDTOList = new ArrayList<>(passageList.size());
        PassageDTO passageDTO;
        for (DevicePassage passage : passageList) {
            passageDTO = new PassageDTO();
            passageDTOList.add(passageDTO);
            BeanUtils.copyProperties(passage, passageDTO);
        }
        return PageUtil.toPageDTO(passageDTOList, pageResult);
    }

    public void create(PassageAddReq req) {
        DevicePassage device = this.passageDeviceRepository.findByDeviceNo(req.getDeviceNo());
        if (null != device) {
            throw new BizException("设备编号已存在");
        }

        device = new DevicePassage();
        device.setDeviceNo(req.getDeviceNo());
        device.setName(req.getName());
        device.setAreaName(req.getAreaName());
        device.setTenantOrgId(req.getTenantOrgId());
        device.setRemark(req.getRemark());
        this.passageDeviceRepository.save(device);
    }

    public void update(PassageEditReq req) {
        DevicePassage device = this.passageDeviceRepository.findById(req.getId()).orElseThrow(() -> new BizException("设备不存在"));

        Long count = this.passageDeviceRepository.countByDeviceNoAndIdNot(req.getDeviceNo(), req.getId());
        if (null != count && count > 0) {
            throw new BizException("设备编号已存在");
        }

        device.setDeviceNo(req.getDeviceNo());
        device.setName(req.getName());
        device.setAreaName(req.getAreaName());
        device.setTenantOrgId(req.getTenantOrgId());
        device.setRemark(req.getRemark());
        this.passageDeviceRepository.save(device);
    }

    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("请选择需要删除的项");
        }

        for (Long id : ids) {
            this.passageDeviceRepository.deleteById(id);
        }
    }
}
