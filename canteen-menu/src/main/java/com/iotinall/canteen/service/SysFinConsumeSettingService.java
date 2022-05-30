package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constants.MealTypeEnum;
import com.iotinall.canteen.dto.finconsumesetting.FinConsumeSettingDTO;
import com.iotinall.canteen.dto.finconsumesetting.FinConsumeSettingReq;
import com.iotinall.canteen.entity.FinConsumeSetting;
import com.iotinall.canteen.repository.FinConsumeSettingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * fin_consume_setting ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-23 17:57:09
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysFinConsumeSettingService {

    @Resource
    private FinConsumeSettingRepository finConsumeSettingRepository;

    public List<FinConsumeSettingDTO> listAll() {
        if (null == finConsumeSettingRepository.findByMealType(MealTypeEnum.BREAKFAST) ||
                null == finConsumeSettingRepository.findByMealType(MealTypeEnum.LUNCH) ||
                null == finConsumeSettingRepository.findByMealType(MealTypeEnum.DINNER)) {
            return Collections.emptyList();
        }
        List<FinConsumeSetting> list = new ArrayList<>();
        list.add(finConsumeSettingRepository.findByMealType(MealTypeEnum.BREAKFAST));
        list.add(finConsumeSettingRepository.findByMealType(MealTypeEnum.LUNCH));
        list.add(finConsumeSettingRepository.findByMealType(MealTypeEnum.DINNER));
        if (list.size() != 3) {
            throw new BizException("消费设置数据错误");
        }
        return list.stream().map((item) -> {
            FinConsumeSettingDTO finConsumeSettingDTO = new FinConsumeSettingDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, finConsumeSettingDTO);
            return finConsumeSettingDTO;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(List<FinConsumeSettingReq> list) throws BindException {
        FinConsumeSetting setting;
        finConsumeSettingRepository.deleteAll();
        for (int i = 0; i < 3; i++) {
            setting = new FinConsumeSetting();
            setting.setBeginTime(list.get(i).getBeginTime());
            setting.setEndTime(list.get(i).getEndTime());
            setting.setCreateId(SecurityUtils.getUserId());
            setting.setCreateTime(LocalDateTime.now());
            if (i == 0) {
                setting.setName("早餐设置");
                setting.setMealType(MealTypeEnum.BREAKFAST);
            } else if (i == 1) {
                setting.setName("午餐设置");
                setting.setMealType(MealTypeEnum.LUNCH);
            } else {
                setting.setName("晚餐设置");
                setting.setMealType(MealTypeEnum.DINNER);
            }
            finConsumeSettingRepository.save(setting);
        }
    }

    /**
     * 获取当前餐厅时间配置
     */
    public String mealTime() {
        return finConsumeSettingRepository.findAll().stream().map(item ->
                new StringBuilder()
                        .append(item.getMealType().getDesc())
                        .append(" ")
                        .append(item.getBeginTime())
                        .append("-")
                        .append(item.getEndTime()).toString()
        ).collect(Collectors.joining(","));
    }
}