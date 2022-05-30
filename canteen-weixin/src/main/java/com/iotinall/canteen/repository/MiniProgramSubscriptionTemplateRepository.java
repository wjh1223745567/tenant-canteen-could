package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MiniProgramSubscriptionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 小程序订阅消息
 *
 * @author loki
 * @date 2020/09/14 10:33
 */
public interface MiniProgramSubscriptionTemplateRepository extends JpaRepository<MiniProgramSubscriptionTemplate, Long>, JpaSpecificationExecutor {
    /**
     * 获取模板
     *
     * @author loki
     * @date 2020/09/27 16:26
     */
    MiniProgramSubscriptionTemplate findByAppIdAndTemplateId(String appId, String templateId);

    /**
     * 获取模板
     *
     * @author loki
     * @date 2020/09/27 17:34
     */
    MiniProgramSubscriptionTemplate findByAppIdAndType(String appId, String type);

    /**
     * 获取模板
     *
     * @author loki
     * @date 2020/09/27 17:34
     */
    List<MiniProgramSubscriptionTemplate> findByAppIdAndTypeIn(String appId, List<String> type);
}
