package com.iotinall.canteen.utils;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * httpclientutil
 *
 * @author loki
 * @date 2021/6/29 17:26
 **/
public class HttpClientUtil {
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            // 信任所有
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null,
                            (TrustStrategy) (chain, authType) -> true)
                    .build();
            return HttpClients.custom()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                    .build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();

    }
}
