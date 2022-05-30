package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.message.WxMiniProgramSubscribeMessage;
import com.iotinall.canteen.dto.message.WxMiniProgramSubscribeMessageData;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信消息通知服务类
 *
 * @author loki
 * @date 2020/09/24 21:06
 */
@Slf4j
public class WxMessageBuilderService {

    private static final String LANG = "zh_CN";

    /**
     * 后厨管理审核人
     * 申请人
     * {{name1.DATA}}
     * 申请部门
     * {{thing8.DATA}}
     * 审核内容
     * {{thing15.DATA}}
     * 审核状态
     * {{phrase16.DATA}}
     *
     * @author loki
     * @date 2020/09/25 14:17
     */
    public static WxMiniProgramSubscribeMessage buildKitchenManagementReviewer(String openId, String templateId, String appName, String role, String content, String state, String pageType) {
        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(4);
        data.put("name1", new WxMiniProgramSubscribeMessageData(appName));
        data.put("thing8", new WxMiniProgramSubscribeMessageData(role));
        data.put("thing15", new WxMiniProgramSubscribeMessageData(content));
        data.put("phrase16", new WxMiniProgramSubscribeMessageData(state));

        return buildMiniProgramSubscribeMsg(openId, pageType, templateId, data);
    }

    /**
     * 后厨管理申请人
     * 申请人 {{thing12.DATA}}
     * 所属部门 {{phrase4.DATA}}
     * 审核内容 {{thing10.DATA}}
     * 审核进度 {{thing15.DATA}}
     *
     * @author loki
     * @date 2020/09/25 14:17
     */
    public static WxMiniProgramSubscribeMessage buildKitchenManagementApplicant(String openId, String templateId, String appName, String role, String content, String state, String pageType) {
        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(4);
        data.put("thing12", new WxMiniProgramSubscribeMessageData(appName));
        data.put("phrase4", new WxMiniProgramSubscribeMessageData(role));
        data.put("thing10", new WxMiniProgramSubscribeMessageData(content));
        data.put("thing15", new WxMiniProgramSubscribeMessageData(state));

        return buildMiniProgramSubscribeMsg(openId, pageType, templateId, data);
    }

    /**
     * 资讯通知提示
     * 标题 {{thing1.DATA}}
     * 内容 {{thing3.DATA}}
     *
     * @author loki
     * @date 2020/09/25 14:17
     */
    public static WxMiniProgramSubscribeMessage buildInformationNotice(String openId, String templateId, String title, String remark, String pageType) {
        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(2);
        data.put("thing1", new WxMiniProgramSubscribeMessageData(title));
        data.put("thing3", new WxMiniProgramSubscribeMessageData(remark));

        return buildMiniProgramSubscribeMsg(openId, pageType, templateId, data);
    }

    /**
     * 点评和饮食记录提醒
     * 点评提醒 {{character_string8.DATA}}
     * 备注 {{thing7.DATA}}
     *
     * @author loki
     * @date 2020/09/25 14:17
     */
    public static WxMiniProgramSubscribeMessage buildCommentsAndFoodMessage(String openId, String templateId, String content, String commentsType, String pageType) {
        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(2);
        data.put("character_string8", new WxMiniProgramSubscribeMessageData(content));
        data.put("thing7", new WxMiniProgramSubscribeMessageData(commentsType));

        return buildMiniProgramSubscribeMsg(openId, pageType, templateId, data);
    }

    /**
     * 构建小程序订阅消息
     *
     * @author loki
     * @date 2020/09/25 13:44
     */
    public static WxMiniProgramSubscribeMessage buildMiniProgramSubscribeMsg(String openId, String redirect, String templateId, Map<String, WxMiniProgramSubscribeMessageData> data) {
        return WxMiniProgramSubscribeMessage.builder()
                .openId(openId)
                .templateId(templateId)
                .page(redirect)
                .data(data)
                .lang(LANG)
                .build();
    }
}
