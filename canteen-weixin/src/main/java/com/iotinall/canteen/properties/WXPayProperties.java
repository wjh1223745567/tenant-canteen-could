package com.iotinall.canteen.properties;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author WJH
 * @date 2019/11/710:05
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pay.wxpay")
public class WXPayProperties extends WXPayConfig {

    private String appId; // appId

    private String mchId; // mchId

    private String key; // api key

    private String tradeType; // trade type

    private String notifyUrl; // notify url

    private String tackOutNotifyUrl; // tack out

    private String certPath = "wxpay/apiclient_cert.p12"; // cert path, absolute path or class path

    private int httpConnectTimeoutMs = 6 * 1000; // connect timeout ms

    private int httpReadTimeoutMs = 8 * 1000; // read timeout ms

    private boolean autoReport = true;

    private int reportWorkerNum = 6;

    private int reportQueueMaxSize = 10000;

    private int reportBatchSize = 10;


    public String getAppId() {
        return appId;
    }

    public String getMchId() {
        return mchId;
    }

    @Override
    protected String getAppID() {
        return appId;
    }

    @Override
    protected String getMchID() {
        return mchId;
    }

    private byte[] CERT_BYTES;

    @Override
    protected InputStream getCertStream() {
        if(CERT_BYTES == null) {
            File file = new File(certPath);
            InputStream is;
            try {
                if(file.isAbsolute()) { // 绝对路径
                    is = new FileInputStream(file);
                } else {
                    is = WXPayProperties.class.getClassLoader().getResourceAsStream(certPath);
                }
                byte[] cache = new byte[1024];
                int len;
                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                while((len = is.read(cache)) != -1) {
                    baos.write(cache, 0, len);
                }
                CERT_BYTES = baos.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException("can't load cert file:" + certPath);
            }
        }
        return new ByteArrayInputStream(CERT_BYTES);
    }

    @Override
    protected IWXPayDomain getWXPayDomain() {
        return payDomain;
    }

    @Override
    public boolean shouldAutoReport() {
        return autoReport;
    }

    private final IWXPayDomain payDomain = new IWXPayDomainImpl();

    private static class IWXPayDomainImpl implements IWXPayDomain {
        private final DomainInfo domainInfo = new DomainInfo(WXPayConstants.DOMAIN_API, true);
        @Override
        public void report(String domain, long elapsedTimeMillis, Exception ex) {

        }

        @Override
        public DomainInfo getDomain(WXPayConfig config) {
            return domainInfo;
        }
    }
}
