package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.messdaily.MessEditReq;
import com.iotinall.canteen.entity.Mess;
import com.iotinall.canteen.repository.MessRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 餐厅 ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysMessService {

    @Resource
    private MessRepository messRepository;

    public Mess detail() {
        return messRepository.findFirstBy();
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(MessEditReq req) {
        Mess record;
        if (req.getId() != null) {
            record = messRepository.findById(req.getId()).orElseThrow(() -> new BizException("记录存在"));
        } else {
            record = new Mess();
        }

        if (!StringUtils.isBlank(req.getName())) {
            record.setName(req.getName());
        }
        if (req.getCapacity() != null) {
            record.setCapacity(req.getCapacity());
        }

        messRepository.save(record);
    }
}