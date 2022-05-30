package com.iotinall.canteen.common.configuration;

import com.iotinall.canteen.common.security.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 实现修改更新自动添加操作人
 */
@Configuration
public class PartnerAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        if(SecurityUtils.getCurrentUser() != null && SecurityUtils.getUserId() != null){
            return Optional.of(SecurityUtils.getUserId());
        }
        return Optional.empty();
    }
}
