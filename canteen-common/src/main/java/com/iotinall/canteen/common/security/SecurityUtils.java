package com.iotinall.canteen.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 登入信息
 */
@Slf4j
public class SecurityUtils {

    public static SecurityUserDetails getCurrentUser() {
        SecurityUserDetails userDetails = null;
        try {
            userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ignored) {
        }
        return userDetails;
    }

    /**
     * 获取当前登入用户ID
     */
    public static Long getUserId() {
        try {
            return getCurrentUser().getEmpId();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取当前登入用户ID
     */
    public static String getOpenId() {
        SecurityUserDetails user = getCurrentUser();
        return null == user ? null : user.getOpenId();
    }

    /**
     * 获取当前登入用户名称
     */
    public static String getUserName() {
        return getCurrentUser().getNickName();
    }

    /**
     * 获取当前登入用户名称
     */
    public static String getUserOrgName() {
        return getCurrentUser().getOrgName();
    }


}
