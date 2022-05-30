package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MiniProgramSubscriptionTemplate;
import com.iotinall.canteen.entity.UserTemplateSubRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户已订阅小程序订阅消息
 *
 * @author loki
 * @date 2020/09/14 10:33
 */
public interface UserTemplateSubRelationRepository extends JpaRepository<UserTemplateSubRelation, Long>, JpaSpecificationExecutor<UserTemplateSubRelation> {
    /**
     * 获取用户订阅消息
     *
     * @author loki
     * @date 2020/09/28 10:15
     */
    UserTemplateSubRelation findByTemplateAndOpenId(MiniProgramSubscriptionTemplate template, String openId);
}
