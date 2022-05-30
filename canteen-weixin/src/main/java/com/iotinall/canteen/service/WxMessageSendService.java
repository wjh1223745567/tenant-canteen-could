package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.constants.StockBillStatusEnum;
import com.iotinall.canteen.constants.StockBillTypeEnum;
import com.iotinall.canteen.constants.WXConstants;
import com.iotinall.canteen.dto.message.WxMiniProgramSubscribeMessage;
import com.iotinall.canteen.dto.message.WxMiniProgramSubscribeMessageData;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.service.base.BaseWxService;
import com.iotinall.canteen.utils.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 微信消息通知服务类
 *
 * @author loki
 * @date 2020/09/24 21:06
 */
@Slf4j
@Service
public class WxMessageSendService extends BaseWxService {
    @Autowired
    private UserTemplateSubRelationRepository userTemplateSubRelationRepository;
    @Autowired
    private MiniProgramSubscriptionTemplateRepository miniProgramSubscriptionTemplateRepository;
    @Autowired
    private WxMessageContenttConfigRepository wxMessageContenttConfigRepository;
    @Autowired
    private WxMessageSendRecordRepository wxMessageSendRecordRepository;
    @Autowired
    private WxMessageSendRecordDetailRepository wxMessageSendRecordDetailRepository;
    @Autowired
    private WxMessageSendRecordService wxMessageSendRecordService;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 获取微信appSecret
     *
     * @author loki
     * @date 2020/09/27 11:16
     */
    public Object getAppSecret() {
        Map<String, String> result = new HashMap<>(2);
        result.put("appId", appId);
        result.put("appSecret", appSecret);
        return result;
    }

    /**
     * 发送就餐信息
     */
    public void sendMealBeginMessage(String openId, WxMessageContentConfig msgContent, String mealTime) {
        //获取模板
        MiniProgramSubscriptionTemplate template = miniProgramSubscriptionTemplateRepository.findByAppIdAndType(appId, WXConstants.TEMPLATE_DINER_BEGIN);
        if (null == template) {
            log.info("微信订阅消息模板不存在");
            return;
        }

        //用户是否订阅
        if (!isSubscribe(openId, template)) {
            log.info("用户未订阅该消息：{}", JSON.toJSONString(template));
            return;
        }
        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(2);
        data.put("thing1", new WxMiniProgramSubscribeMessageData(msgContent.getContent()));
        data.put("time5", new WxMiniProgramSubscribeMessageData(mealTime));

        this.sendMessage(WxMessageBuilderService.buildMiniProgramSubscribeMsg(openId, msgContent.getRedirect(), template.getTemplateId(), data));
    }

    /**
     * 商城取货提示
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendShoppingPickupMessage(List<Long> employeeListIds, String goodsName) {
        WxMessageContentConfig msgContent = this.wxMessageContenttConfigRepository.findByType(WXConstants.MSG_CONTENT_TYPE_SHOPPING_PICKUP);
        if (null == msgContent || !msgContent.getOpen()) {
            log.info("微信消息未配置或者已关闭");
            return;
        }

        //获取模板
        MiniProgramSubscriptionTemplate template = miniProgramSubscriptionTemplateRepository.findByAppIdAndType(appId, WXConstants.TEMPLATE_SHOPPING_PICKUP);
        if (null == template) {
            log.info("微信订阅模板不存在");
            return;
        }
        Map<Long, FeignEmployeeDTO> map = feignEmployeeService.findByIds(new HashSet<>(employeeListIds));

        for (Long aLong : map.keySet()) {
            FeignEmployeeDTO employee = map.get(aLong);
            if (StringUtils.isBlank(employee.getOpenid())) {
                continue;
            }

            try {
                if (!isSubscribe(employee.getOpenid(), template)) {
                    log.info("用户未订阅该消息：{}", JSON.toJSONString(template));
                    continue;
                }

                Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(2);
                data.put("thing1", new WxMiniProgramSubscribeMessageData(goodsName));
                data.put("thing3", new WxMiniProgramSubscribeMessageData(msgContent.getRemark()));

                this.sendMessage(WxMessageBuilderService.buildMiniProgramSubscribeMsg(employee.getOpenid(), msgContent.getRedirect(), template.getTemplateId(), data));
            } catch (Exception ex) {
                log.info("微信消息发送失败");
            }

            WxMessageSendRecord record = new WxMessageSendRecord();
            record.setConfig(msgContent);
            record.setCreateTime(LocalDateTime.now());
            record = this.wxMessageSendRecordRepository.save(record);

            WxMessageSendRecordDetail recordDetail = new WxMessageSendRecordDetail();
            recordDetail.setContent(wxMessageSendRecordService.buildShoopingPickupContent(goodsName, msgContent.getRemark()));
            recordDetail.setOpenId(employee.getOpenid());
            recordDetail.setRecord(record);
            recordDetail.setCreateTime(LocalDateTime.now());
            this.wxMessageSendRecordDetailRepository.save(recordDetail);
        }
    }

    /**
     * 商城取货提示
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendShoppingDeadlineMessage(List<Long> employeeListIds) {
        WxMessageContentConfig msgContent = this.wxMessageContenttConfigRepository.findByType(WXConstants.MSG_CONTENT_TYPE_SHOPPING_DEADLINE);
        if (null == msgContent || !msgContent.getOpen()) {
            log.info("微信消息未配置或者已关闭");
            return;
        }

        //获取模板
        MiniProgramSubscriptionTemplate template = miniProgramSubscriptionTemplateRepository.findByAppIdAndType(appId, WXConstants.TEMPLATE_SHOPPING_DEADLINE);
        if (null == template) {
            log.info("微信订阅模板不存在");
            return;
        }

        Map<Long, FeignEmployeeDTO> map = feignEmployeeService.findByIds(new HashSet<>(employeeListIds));

        for (Long aLong : map.keySet()) {
            FeignEmployeeDTO employee = map.get(aLong);
            if (StringUtils.isBlank(employee.getOpenid())) {
                continue;
            }

            try {
                if (!isSubscribe(employee.getOpenid(), template)) {
                    log.info("用户未订阅该消息：{}", JSON.toJSONString(template));
                    continue;
                }

                Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(2);
                data.put("thing1", new WxMiniProgramSubscribeMessageData(msgContent.getContent()));
                data.put("time2", new WxMiniProgramSubscribeMessageData(msgContent.getRemark()));

                this.sendMessage(WxMessageBuilderService.buildMiniProgramSubscribeMsg(employee.getOpenid(), msgContent.getRedirect(), template.getTemplateId(), data));
            } catch (Exception ex) {
                log.info("微信消息发送失败");
            }

            WxMessageSendRecord record = new WxMessageSendRecord();
            record.setConfig(msgContent);
            record.setCreateTime(LocalDateTime.now());
            record = this.wxMessageSendRecordRepository.save(record);

            WxMessageSendRecordDetail recordDetail = new WxMessageSendRecordDetail();
            recordDetail.setContent(wxMessageSendRecordService.buildShopingDeadline(msgContent.getContent(), msgContent.getRemark()));
            recordDetail.setOpenId(employee.getOpenid());
            recordDetail.setRecord(record);
            recordDetail.setCreateTime(LocalDateTime.now());
            this.wxMessageSendRecordDetailRepository.save(recordDetail);
        }
    }

    /**
     * 余额不足
     *
     * @author loki
     * @date 2021/04/15 17:56
     */
    public void sendBalanceNotEnoughMessage1(Long employeeId, BigDecimal balance) {
        FeignEmployeeDTO feignEmployeeDTO = feignEmployeeService.findById(employeeId);
        if (StringUtils.isBlank(feignEmployeeDTO.getOpenid())) {
            log.info("openId 为空");
            return;
        }

        WxMessageContentConfig msgContent = this.wxMessageContenttConfigRepository.findByType(WXConstants.MSG_CONTENT_TYPE_BALANCE);
        if (null == msgContent || !msgContent.getOpen()) {
            log.info("微信消息未配置或者已关闭");
            return;
        }

        try {
            if (balance.compareTo(new BigDecimal(msgContent.getRemark())) < 0) {
                sendBalanceNotEnoughMessage(employeeId, msgContent, balance);
            }
        } catch (Exception ex) {
            log.info("发送余额不足消息失败");
        }
    }

    /**
     * 余额不足
     *
     * @author loki
     * @date 2021/04/15 17:56
     */
    private void sendBalanceNotEnoughMessage(Long employeeId, WxMessageContentConfig msgContent, BigDecimal balance) {
        FeignEmployeeDTO feignEmployeeDTO = feignEmployeeService.findById(employeeId);

        //获取模板
        MiniProgramSubscriptionTemplate template = miniProgramSubscriptionTemplateRepository.findByAppIdAndType(appId, WXConstants.TEMPLATE_BALANCE_NOT_ENOUGH);
        if (null == template) {
            log.info("微信订阅模板不存在");
            return;
        }

        //用户是否订阅
        if (!isSubscribe(feignEmployeeDTO.getOpenid(), template)) {
            log.info("用户未订阅该消息：{}", JSON.toJSONString(template));
            return;
        }

        //今日是否已经发送
        WxMessageSendRecordDetail detail;
        WxMessageSendRecord record = wxMessageSendRecordRepository.findSendRecord(WXConstants.MSG_CONTENT_TYPE_BALANCE, LocalDate.now());
        if (record != null) {
            detail = wxMessageSendRecordDetailRepository.findByRecordAndOpenId(record, feignEmployeeDTO.getOpenid());
            if (null != detail) {
                log.info("余额不足提醒，今日通知次数已到达上限");
                return;
            }
        } else {
            record = new WxMessageSendRecord();
            record.setConfig(msgContent);
            record.setCreateTime(LocalDateTime.now());
            record = wxMessageSendRecordRepository.save(record);

            detail = new WxMessageSendRecordDetail();
            detail.setRecord(record);
            detail.setOpenId(feignEmployeeDTO.getOpenid());
            detail.setContent(wxMessageSendRecordService.buildBalanceNotEnoughContent(msgContent.getContent(), balance + "元"));
            wxMessageSendRecordDetailRepository.save(detail);
        }

        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(2);
        data.put("thing1", new WxMiniProgramSubscribeMessageData(msgContent.getContent()));
        data.put("amount2", new WxMiniProgramSubscribeMessageData(balance + "元"));

        this.sendMessage(WxMessageBuilderService.buildMiniProgramSubscribeMsg(feignEmployeeDTO.getOpenid(), msgContent.getRedirect(), template.getTemplateId(), data));
    }

    /**
     * 发送经销存单据审核消息
     *
     * @author loki
     * @date 2020/09/25 16:10
     */
    @Async
    public void sendStockAuditMessage(Set<String> receiveMessageOpenIdList,
                                      String billNo,
                                      String applyUserName,
                                      LocalDate billDate,
                                      String billType,
                                      String remark,
                                      Integer msgType) {
        WxMessageContentConfig msgContent = this.wxMessageContenttConfigRepository.findByType(msgType);
        if (null == msgContent || !msgContent.getOpen()) {
            log.info("微信消息未配置或者已关闭");
            return;
        }

        if (CollectionUtils.isEmpty(receiveMessageOpenIdList)) {
            return;
        }


        for (String openId : receiveMessageOpenIdList) {
            if (StringUtils.isBlank(openId)) {
                log.info("openid 为空");
                continue;
            }

            //获取模板
            MiniProgramSubscriptionTemplate template = miniProgramSubscriptionTemplateRepository.findByAppIdAndType(appId, WXConstants.TEMPLATE_STOCK_BILL_AUDIT);
            if (null == template) {
                log.info("微信订阅消息模板不存在");
                return;
            }

            //用户是否订阅
            if (!isSubscribe(openId, template)) {
                log.info("用户未订阅该消息：{}", JSON.toJSONString(template));
                return;
            }

            /**
             * 单号 {{character_string7.DATA}}
             * 申请人 {{name1.DATA}}
             * 申请时间 {{time2.DATA}}
             * 申请类型 {{thing10.DATA}}
             * 申请备注 {{thing3.DATA}}
             */
            Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(5);
            data.put("character_string7", new WxMiniProgramSubscribeMessageData(billNo));
            data.put("name1", new WxMiniProgramSubscribeMessageData(applyUserName));
            data.put("time2", new WxMiniProgramSubscribeMessageData(LocalDateUtil.format(billDate)));
            data.put("thing10", new WxMiniProgramSubscribeMessageData(StockBillTypeEnum.getBillTypeName(billType)));
            data.put("thing3", new WxMiniProgramSubscribeMessageData(StringUtils.isBlank(remark) ? "无" : remark));

            this.sendMessage(WxMessageBuilderService.buildMiniProgramSubscribeMsg(
                    openId,
                    msgContent.getRedirect() + "?billNo=" + billNo,
                    template.getTemplateId(),
                    data));
        }
    }

    /**
     * 发送经销存单据状态变动消息
     *
     * @author loki
     * @date 2020/09/25 16:10
     */
    @Async
    public void sendBillStatusChangedMessage(String openId,
                                             String billNo,
                                             Integer status,
                                             String optUserName,
                                             String billType,
                                             String remark,
                                             Integer msgType
    ) {
        WxMessageContentConfig msgContent = this.wxMessageContenttConfigRepository.findByType(msgType);
        if (null == msgContent || !msgContent.getOpen()) {
            log.info("微信消息未配置或者已关闭");
            return;
        }

        if (StringUtils.isBlank(openId)) {
            log.info("openid 为空");
            return;
        }

        //获取模板
        MiniProgramSubscriptionTemplate template = miniProgramSubscriptionTemplateRepository.findByAppIdAndType(appId, WXConstants.TEMPLATE_STOCK_BILL_APPLY);
        if (null == template) {
            log.info("微信订阅消息模板不存在");
            return;
        }

        //用户是否订阅
        if (!isSubscribe(openId, template)) {
            log.info("用户未订阅该消息：{}", JSON.toJSONString(template));
            return;
        }

        /**
         * 申请单号 {{character_string8.DATA}}
         * 申请类型 {{name2.DATA}}
         * 审批状态 {{phrase5.DATA}}
         * 审批人 {{thing16.DATA}}
         * 备注 {{thing7.DATA}}
         */
        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(5);
        data.put("character_string8", new WxMiniProgramSubscribeMessageData(billNo));
        data.put("name2", new WxMiniProgramSubscribeMessageData(StockBillTypeEnum.getBillTypeName(billType)));
        data.put("phrase5", new WxMiniProgramSubscribeMessageData(StockBillStatusEnum.getBillStatusName(status)));
        data.put("thing16", new WxMiniProgramSubscribeMessageData(optUserName));
        data.put("thing7", new WxMiniProgramSubscribeMessageData(StringUtils.isBlank(remark) ? "无" : remark));

        this.sendMessage(WxMessageBuilderService.buildMiniProgramSubscribeMsg(openId,
                msgContent.getRedirect() + "?billNo=" + billNo,
                template.getTemplateId(),
                data));
    }

    /**
     * 发送意见反馈提醒消息
     */
    public void sendFeedbackNotice(String openId, String content) {
        if (StringUtils.isBlank(openId)) {
            return;
        }

        WxMessageContentConfig msgContent = this.wxMessageContenttConfigRepository.findByType(WXConstants.MSG_CONTENT_TYPE_FEEDBACK);
        if (null == msgContent || !msgContent.getOpen()) {
            log.info("微信消息未配置或者已关闭");
            return;
        }

        //获取模板
        MiniProgramSubscriptionTemplate template = miniProgramSubscriptionTemplateRepository.findByAppIdAndType(appId, WXConstants.TEMPLATE_FEEDBACK_NOTICE);
        if (null == template) {
            log.info("微信订阅模板不存在");
            return;
        }

        //用户是否订阅
        if (!isSubscribe(openId, template)) {
            log.info("用户未订阅该消息：{}", JSON.toJSONString(template));
            return;
        }

        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(3);
        data.put("thing9", new WxMiniProgramSubscribeMessageData(content));
        data.put("thing10", new WxMiniProgramSubscribeMessageData(msgContent.getContent()));
        data.put("thing5", new WxMiniProgramSubscribeMessageData(msgContent.getRemark()));

        this.sendMessage(WxMessageBuilderService.buildMiniProgramSubscribeMsg(openId, msgContent.getRedirect(), template.getTemplateId(), data));

        WxMessageSendRecord record = new WxMessageSendRecord();
        record.setConfig(msgContent);
        record.setCreateTime(LocalDateTime.now());
        record = this.wxMessageSendRecordRepository.save(record);

        WxMessageSendRecordDetail detail = new WxMessageSendRecordDetail();
        detail.setRecord(record);
        detail.setCreateTime(LocalDateTime.now());
        detail.setContent(wxMessageSendRecordService.buildFeedbackContent(content, msgContent.getContent(), msgContent.getRemark()));
        detail.setOpenId(openId);
        this.wxMessageSendRecordDetailRepository.save(detail);
    }

    /**
     * 校验用户是否已经订阅消息
     *
     * @author loki
     * @date 2020/09/27 16:57
     */
    private Boolean isSubscribe(String openId, MiniProgramSubscriptionTemplate template) {
        UserTemplateSubRelation result = userTemplateSubRelationRepository.findByTemplateAndOpenId(template, openId);
        return result != null;
    }

    /**
     * 微信订阅消息测试
     *
     * @author loki
     * @date 2020/09/28 18:59
     */
    public void test() {
        String openId = "oy4AC5dHDUH6ySJbtHJZA4lugl3E";
        String templateId = "qYIJZsgg7PsvQ3CHnavrK3xKS4Fb4aej6xnGCxa6jWc";

        Map<String, WxMiniProgramSubscribeMessageData> data = new HashMap<>(5);
        data.put("character_string2", new WxMiniProgramSubscribeMessageData("00001"));
        data.put("name1", new WxMiniProgramSubscribeMessageData("测试商家"));

        WxMiniProgramSubscribeMessage message = WxMiniProgramSubscribeMessage.builder()
                .openId(openId)
                .templateId(templateId)
                .page(null)
                .data(data)
                .miniprogramState("formal")
                .lang("zh_CN")
                .build();

        this.sendMessage(message);
    }
}