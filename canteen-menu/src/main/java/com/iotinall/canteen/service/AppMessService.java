package com.iotinall.canteen.service;

import com.iotinall.canteen.entity.Mess;
import com.iotinall.canteen.repository.MessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author bingo
 * @date 1/10/2020 20:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppMessService {
    @Resource
    private MessRepository messRepository;

    public Mess getDetail() {
        return messRepository.findFirstBy();
    }
}
