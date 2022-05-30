package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.sendmessage.MessageContent;
import com.iotinall.canteen.dto.sendmessage.MessageContentDetail;
import com.iotinall.canteen.entity.WxMessageSendRecordDetail;
import com.iotinall.canteen.repository.WxMessageSendRecordDetailRepository;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信发送消息记录
 *
 * @author loki
 * @date 2021/04/17 18:02
 */
@Slf4j
@Service
public class WxMessageSendRecordService {
    @Resource
    private WxMessageSendRecordDetailRepository wxMessageSendRecordDetailRepository;

    /**
     * 小程序获取消息列表
     *
     * @author loki
     * @date 2021/04/17 18:03
     */
    public Object getAppMessageRecordPage(Pageable page) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.eq("openId", SecurityUtils.getOpenId()));
        Page<WxMessageSendRecordDetail> pageResult = wxMessageSendRecordDetailRepository.findAll(builder.build(), page);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(pageResult);
        }

        List<MessageContent> result = new ArrayList<>();
        MessageContent content;
        List<MessageContentDetail> contentDetailList;
        for (WxMessageSendRecordDetail detail : pageResult.getContent()) {
            content = new MessageContent();
            result.add(content);
            content.setCreateTime(LocalDateTimeUtil.localDatetime2Str(detail.getCreateTime()));

            if (null != detail.getRecord().getConfig()) {
                content.setTypeName(detail.getRecord().getConfig().getTypeName());

                if (StringUtils.isNotBlank(detail.getRecord().getConfig().getRedirect())) {
                    String split = "[?]";
                    String[] arrs = detail.getRecord().getConfig().getRedirect().split(split);
                    content.setRedirect(arrs[1].split("=")[1]);
                }
            }

            if (StringUtils.isNotBlank(detail.getContent())) {
                contentDetailList = JSON.parseArray(detail.getContent(), MessageContentDetail.class);
                content.setContent(contentDetailList);
            }
        }
        return PageUtil.toPageDTO(result, pageResult);
    }

    /**
     * 构建早中晚content
     *
     * @author loki
     * @date 2021/04/17 18:25
     */
    public String buildMealBeginContent(String content, String mealTime) {
        List<MessageContentDetail> result = new ArrayList<>();
        result.add(new MessageContentDetail("温馨提示", content));
        result.add(new MessageContentDetail("就餐时间", mealTime));
        return JSON.toJSONString(result);
    }

    /**
     * 构建商城取货
     *
     * @author loki
     * @date 2021/04/17 18:25
     */
    public String buildShoopingPickupContent(String goodsName, String remark) {
        List<MessageContentDetail> result = new ArrayList<>();
        result.add(new MessageContentDetail("商品名称", goodsName));
        result.add(new MessageContentDetail("备注", remark));
        return JSON.toJSONString(result);
    }

    /**
     * 构建商城取货
     *
     * @author loki
     * @date 2021/04/17 18:25
     */
    public String buildBalanceNotEnoughContent(String remark, String balance) {
        List<MessageContentDetail> result = new ArrayList<>();
        result.add(new MessageContentDetail("温馨提示", remark));
        result.add(new MessageContentDetail("前余额", balance));
        return JSON.toJSONString(result);
    }

    /**
     * 构建反馈
     *
     * @author loki
     * @date 2021/04/17 18:25
     */
    public String buildFeedbackContent(String content, String handleResult, String remark) {
        List<MessageContentDetail> result = new ArrayList<>();
        result.add(new MessageContentDetail("反馈内容", content));
        result.add(new MessageContentDetail("处理结果", handleResult));
        result.add(new MessageContentDetail("备注", remark));
        return JSON.toJSONString(result);
    }

    /**
     * 构建商城截止订购
     *
     * @author loki
     * @date 2021/04/17 18:25
     */
    public String buildShopingDeadline(String content, String openTime) {
        List<MessageContentDetail> result = new ArrayList<>();
        result.add(new MessageContentDetail("温馨提示", content));
        result.add(new MessageContentDetail("开放时间", openTime));
        return JSON.toJSONString(result);
    }
}
