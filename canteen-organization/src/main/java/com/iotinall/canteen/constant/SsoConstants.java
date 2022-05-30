package com.iotinall.canteen.constant;

/**
 * 单点登录使用常量
 */
public interface SsoConstants {

    /**
     * 登录路径
     */
    String LOGIN_URL = "/corona/oauth2/v1/login";

    /**
     * 认证注销
     */
    String SYS_LOGOUT = "/corona/oauth2/v1/logout";

    /**
     * web请求授权
     */
    String WEB_AUTHORIZE = "/corona/oauth2/v1/authorize";

    /**
     * 获取系统授权信息
     */
    String SYS_AUTHORIZE = "/corona/oauth2/v1/access_token";

    /**
     * 刷新token
     */
    String SYS_REFRESH_TOKEN = "/corona/oauth2/v1/refresh_token";

    /**
     * 查询授权接口
     */
    String SYS_AUTHORIZE_INFO = "/corona/oauth2/v1/token_info";

    /**
     * 验证授权信息
     */
    String SYS_AUTHORIZE_VALID = "/corona/oauth2/v1/token_valid";

    /**
     * 获取用户信息接口
     */
    String SYS_USER_INFO = "/corona/oauth2/v1/user_info";

}
