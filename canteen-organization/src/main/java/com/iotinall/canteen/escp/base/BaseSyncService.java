package com.iotinall.canteen.escp.base;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.util.UUIDUtil;
import com.iotinall.canteen.constant.DateTimeFormatters;
import com.iotinall.canteen.constant.EscpErrorCodeEnum;
import com.iotinall.canteen.dto.escp.ReqDTO;
import com.iotinall.canteen.dto.escp.RespDTO;
import com.iotinall.canteen.property.EscpProperty;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import io.undertow.util.Headers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * 同步基类
 *
 * @author loki
 * @date 2021/6/21 10:58
 **/
@Slf4j
@Service
public class BaseSyncService {
    @Resource
    protected OrgEmployeeRepository orgEmployeeRepository;
    @Resource
    protected CloseableHttpClient closeableHttpClient;
    @Resource
    protected EscpProperty escpProperty;
    @Resource
    protected PasswordEncoder passwordEncoder;

    protected static final String API_VERSION_LIST = "version-list";
    protected static final String API_ORG = "org-list";
    protected static final String API_EMP = "emp-list";
    protected static final String API_OPEN_CARD_LIST = "open-card-list";
    protected static final String API_TRANSACTION_RECORD = "transaction-record-list";
    protected static final String API_BALANCE = "balance";
    protected static final String API_CONSUME = "consume";
    protected static final String API_CONSUME_RESULT = "consume-result";
    protected static final String API_RECHARGE_CHECK = "recharge-check";
    protected static final String API_RECHARGE_APPLY = "recharge-apply";
    protected static final String API_RECHARGE_SUBMIT = "recharge-submit";
    protected static final String API_RECHARGE_STATUS = "recharge-status";

    private static final String APPID = "";
    private static final String DEP_CODE = "";
    private static final String SIGN_KEY = "4E6BCDC262A349CAAA6663791E43C7F6";

    /**
     * 加密
     *
     * @author loki
     * @date 2021/6/21 14:25
     **/
    protected String encode(String data) {
        try {
            byte[] cipherBytes = initCipher().doFinal(data.getBytes(Charset.defaultCharset()));
            String cipherString = Base64.encode(cipherBytes);
            log.info("加密后内容：{}", cipherString);
            return cipherString;
        } catch (Exception ex) {
            throw new BizException("加密失败");
        }
    }

    /**
     * 解密
     *
     * @author loki
     * @date 2021/6/21 14:26
     **/
    protected String decode(String data) {
        if (StringUtils.isBlank(data)) {
            return data;
        }

        try {
            byte[] resultBytes = initCipher().doFinal(Base64.decode(data));
            String result = new String(resultBytes, Charset.defaultCharset());
            log.info("解密后数据：{}", result);
            return result;
        } catch (Exception ex) {
            throw new BizException("加密失败");
        }
    }

    /**
     * 初始化加密cipher
     *
     * @author loki
     * @date 2021/6/21 14:35
     **/
    private Cipher initCipher() {
        try {
            //密钥数据
            byte[] keyData = Base64.decode("X3JxViqrduVAobnikn1ICc+md5LjBxbx");
            //算法名称
            String algorithm = "DESede";
            //填充模式
            String fullAlg = "CBC/PKCS5Padding";
            Cipher cipher = Cipher.getInstance(algorithm + "/" + fullAlg);
            //设置初始向量
            int blockSize = cipher.getBlockSize();
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; i++) {
                iv[i] = 0;
            }
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyData, algorithm), new IvParameterSpec(iv));
            return cipher;
        } catch (Exception ex) {
            throw new BizException("加密失败");
        }
    }

    /**
     * 签名,详情请参考对接文档
     *
     * @author loki
     * @date 2021/6/21 14:45
     **/
    protected String sign(ReqDTO req) {
        return encodeSHA256(buildSignParam(req));
    }

    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @author loki
     * @date 2021/6/21 15:30
     **/
    private static String encodeSHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(Charset.defaultCharset()));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @author loki
     * @date 2021/6/21 15:28
     **/
    private static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString();
    }

    /**
     * 构建签名参数
     *
     * @author loki
     * @date 2021/6/21 14:51
     **/
    private String buildSignParam(ReqDTO req) {
        StringBuilder builder = new StringBuilder();
        builder.append("appid=").append(req.getAppId()).append("&");
        builder.append("ctdate=").append(req.getCtDate()).append("&");
        if (StringUtils.isNotBlank(req.getData())) {
            builder.append("data=").append(encode(req.getData())).append("&");
        }
        builder.append("dpcode=").append(req.getDpCode()).append("&");
        builder.append("msgid=").append(req.getMsgId()).append("&");
        builder.append("key=").append(SIGN_KEY);
        return builder.toString();
    }

    /**
     * 执行请求
     *
     * @author loki
     * @date 2021/6/22 14:24
     **/
    protected RespDTO execute(String url, Object param) {
        ReqDTO req = new ReqDTO()
                .setAppId(APPID)
                .setData(encode(JSON.toJSONString(param)))
                .setCtDate(LocalDateTime.now().format(DateTimeFormatters.YYYYMMDDHHMMSS))
                .setDpCode(DEP_CODE)
                .setMsgId(UUIDUtil.generateUuid());
        req.setSign(sign(req));

        try {
            StringEntity body = new StringEntity(JSON.toJSONString(req));
            HttpUriRequest httpUriRequest = RequestBuilder.post(url)
                    .addHeader(Headers.CONTENT_TYPE_STRING, MediaType.APPLICATION_JSON_VALUE)
                    .setEntity(body).build();
            return parseResult(closeableHttpClient.execute(httpUriRequest));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("请求失败");
        }
    }

    /**
     * 解析请求结果
     *
     * @author loki
     * @date 2021/6/22 14:27
     **/
    private RespDTO parseResult(CloseableHttpResponse response) {
        int code = response.getStatusLine().getStatusCode();
        if (code != EscpErrorCodeEnum.SUCCESS_200.getCode()) {
            log.info("请求失败：{}", EscpErrorCodeEnum.getByCode(code));
            throw new BizException("请求失败");
        }

        try {
            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            return JSON.parseObject(json, new TypeReference<RespDTO>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("解析返回结果失败");
        }
    }

    public static void main(String[] args) {
        Long timestamp = System.currentTimeMillis();
        System.out.println("timestamp:" + timestamp);

        String SecretKey = "api_20210616171706";
        String SecretValue = "8jchhe59sw5i83af";

        String splicing = SecretKey + ":" + SecretValue + ":" + timestamp;
        String sign = DigestUtils.md5DigestAsHex(splicing.toUpperCase().getBytes(StandardCharsets.UTF_8));
        System.out.println("sign:" + sign);
    }
}
