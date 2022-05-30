package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.entity.MiniProgramSubscriptionTemplate;
import com.iotinall.canteen.repository.MiniProgramSubscriptionTemplateRepository;
import com.iotinall.canteen.service.base.BaseWxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 微信小程序订阅消息处理类
 *
 * @author loki
 * @date 2020/09/27 17:30
 */
@Service
public class MiniProgramSubscriptionService extends BaseWxService {
    @Autowired
    private MiniProgramSubscriptionTemplateRepository miniProgramSubscriptionRepository;

    /**
     * 获取微信小程序订阅消息模板ID
     *
     * @author loki
     * @date 2020/09/27 17:28
     */
    public Object getMiniProgramSubTemplate(String type) {
        if (StringUtils.isBlank(type)) {
            throw new BizException("消息类型不能为空");
        }
        List<String> typeList = Arrays.asList(type.split(","));
        List<MiniProgramSubscriptionTemplate> templateList = miniProgramSubscriptionRepository.findByAppIdAndTypeIn(appId, typeList);
        if (CollectionUtils.isEmpty(templateList)) {
            return Collections.EMPTY_LIST;
        }
        return templateList;
    }
}
