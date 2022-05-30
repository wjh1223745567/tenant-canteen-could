package com.iotinall.canteen.service;

import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.entity.FinConsumeSetting;
import com.iotinall.canteen.repository.FinConsumeSettingRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author bingo
 * @date 11/27/2019 14:35
 */
@Service
@Transactional
public class AppFinConsumeSettingService {
    @Resource
    private FinConsumeSettingRepository finConsumeSettingRepository;

    public FinConsumeSetting getCurrentSetting(LocalDateTime time) {
        List<FinConsumeSetting> list = this.list();
        String timeStr = String.format("%02d", time.getHour()) + String.format("%02d", time.getMinute());
        FinConsumeSetting currSetting = null;
        for(int i=0; i< list.size(); i++) {
            FinConsumeSetting setting = list.get(i);
            int tmp = setting.getBeginTime().compareTo(timeStr);
            int tmp2 = setting.getEndTime().compareTo(timeStr);
            if(tmp <= 0 && tmp2 >= 0) { // 恰好在就餐区间
                currSetting = setting;
                break;
            }
        }
        if(currSetting == null) {
            currSetting = list.get(0);
        }
        // 待处理不在用餐期间
        return currSetting;
    }

    public List<FinConsumeSetting> list() {
        List<FinConsumeSetting> list = finConsumeSettingRepository.findAll();
        // 按时间排序
        list.sort(Comparator.comparing(FinConsumeSetting::getBeginTime));
        return list;
    }
}
