package com.iotinall.canteen.utils.sms;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsResultBase;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.HTTPException;
import com.iotinall.canteen.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;

import java.io.IOException;

/**
 * 腾讯云短信发送工具类
 *
 * @author loki
 * @date 2020/04/29 11:14
 */
@Slf4j
public class QCloudSmsUtil {
    /**
     * 发送短信
     *
     * @author loki
     * @date 2020/04/29 11:31
     */
    public static void sendMessage(Boolean isSingle, SmsForm msg, SmsConfig config) {
        SmsResultBase result;
        try {
            // 是否单发
            if (isSingle) {
                SmsSingleSender sender = new SmsSingleSender(config.getAppId(), config.getAppKey());
                result = sender.sendWithParam(SmsConstants.NATION_CODE, msg.getMobile(), config.getTemplateId(), msg.getCaptcha(), config.getSign(), "", "");
            } else {
                SmsMultiSender sender = new SmsMultiSender(config.getAppId(), config.getAppKey());
                result = sender.sendWithParam(SmsConstants.NATION_CODE, msg.getMobiles(), config.getTemplateId(), msg.getCaptcha(), config.getSign(), "", "");
            }
            log.info("短信发送结果【{}】", result);
        } catch (HTTPException e) {
            e.printStackTrace();
            throw new BizException("", "HTTP响应码错误");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BizException("", "json解析错误");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("", "网络IO错误");
        }
    }

    public static void main(String[] args) {
        SmsForm msg = new SmsForm();
        msg.setCaptcha(new String[]{"123456", "2"});
        msg.setMobile("18304040453");
        SmsConfig config = new SmsConfig();
        config.setAppId(SmsConstants.APP_ID);
        config.setAppKey(SmsConstants.APP_KEY);
        config.setTemplateId(SmsConstants.TEMPLATE_ID);
        config.setSign(SmsConstants.SIGN);
        QCloudSmsUtil.sendMessage(true, msg, config);
    }
}
