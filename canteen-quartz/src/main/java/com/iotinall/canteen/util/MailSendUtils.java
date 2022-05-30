package com.iotinall.canteen.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MailSendUtils {

    public static JavaMailSender javaMailSender;

    private static String from;

    /**
     * 发送邮件
     * @param to
     * @param subject 主题
     * @param content 内容
     */
    public static void sendSimpleMail(String to, String subject, String content) {
        for (String s : to.split(",")) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(s);//收信人
            message.setSubject(subject);//主题
            message.setText(content);//内容
            message.setFrom(from);//发信人
            javaMailSender.send(message);
        }

    }

    @Resource
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        MailSendUtils.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    public void setFrom(String from) {
        MailSendUtils.from = from;
    }
}
