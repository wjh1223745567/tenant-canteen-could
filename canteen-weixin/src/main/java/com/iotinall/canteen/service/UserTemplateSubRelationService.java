package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.entity.MiniProgramSubscriptionTemplate;
import com.iotinall.canteen.entity.UserTemplateSubRelation;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.MiniProgramSubscriptionTemplateRepository;
import com.iotinall.canteen.repository.UserTemplateSubRelationRepository;
import com.iotinall.canteen.service.base.BaseWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户已订阅小程序订阅消息
 *
 * @author loki
 * @date 2020/09/14 10:33
 */
@Service
public class UserTemplateSubRelationService extends BaseWxService {
    @Autowired
    private UserTemplateSubRelationRepository userTemplateSubRelationRepository;
    @Autowired
    private FeignEmployeeService feignEmployeeService;
    @Autowired
    private MiniProgramSubscriptionTemplateRepository miniProgramSubscriptionRepository;

    /**
     * 保存用户订阅的小程序消息
     *
     * @author loki
     * @date 2020/09/27 16:20
     */
    public void saveUserSubTemplate(String[] templateIds) {
        UserTemplateSubRelation userTemplateSubRelation;

        for (String templateId : templateIds) {
            //获取模板
            MiniProgramSubscriptionTemplate template = miniProgramSubscriptionRepository.findByAppIdAndTemplateId(appId, templateId);
            if (null == template) {
                throw new BizException("", "微信小程序订阅消息模板不存在");
            }

            userTemplateSubRelation = userTemplateSubRelationRepository.findByTemplateAndOpenId(template, SecurityUtils.getOpenId());
            if (null == userTemplateSubRelation) {
                userTemplateSubRelation = new UserTemplateSubRelation();
                userTemplateSubRelation.setOpenId(SecurityUtils.getOpenId());
                userTemplateSubRelation.setTemplate(template);
                userTemplateSubRelation.setCreateTime(LocalDateTime.now());

                this.userTemplateSubRelationRepository.save(userTemplateSubRelation);
            }
        }
    }
}
