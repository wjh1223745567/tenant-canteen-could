package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.constant.SsoConstants;
import com.iotinall.canteen.dto.sso.*;
import com.iotinall.canteen.property.SsoProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 单点登录对接接口
 */

@Slf4j
@Service
@RefreshScope
public class SsoService {

    @Resource
    private CloseableHttpClient closeableHttpClient;

    @Resource
    private SsoProperty ssoProperty;

    /**
     * 登录接口
     * @param username
     * @param password
     * @return
     */
    public SsoUserInfoResp ssoLogin(String username, String password){
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw new BizException("", "用户名或密码不能为空");
        }
        SsoLoginResp ssoLoginResp = ssoSysLogin(username, password);
        SsoUserInfoResp ssoUserInfoResp = findUserInfo(ssoLoginResp.getCosmosAccessToken(), ssoLoginResp.getCosmosRefreshToken());
        return ssoUserInfoResp;
    }

    private String getUrl(String path){
        return ssoProperty.getSsoUrl() + path;
    }

    /**
     * web获取授权url
     * @return
     */
    public String getAuthUrl(){
        StringBuilder stringBuilder = new StringBuilder()
                .append(getUrl(SsoConstants.WEB_AUTHORIZE))
                .append("?redirect_uri=").append(ssoProperty.getBackUrl())
                .append("&state=loginstate")
                .append("&app_id=").append(ssoProperty.getAppId())
                .append("&response_type=authorization _code&timestamp=").append(System.currentTimeMillis());
        return stringBuilder.toString();
    }

    /**
     * 登录
     * @return
     */

    private SsoLoginResp ssoSysLogin(String username, String password){
        try {
            SsoLoginReq req = new SsoLoginReq()
                    .setApp_id(ssoProperty.getAppId())
                    .setUser_name(username)
                    .setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));

            Long currentTime = System.currentTimeMillis();
            String md5 = md5(Arrays.asList(req.getApp_id(), ssoProperty.getAppSecurity(), req.getUser_name(), req.getPassword(), String.valueOf(currentTime)));

            Map<String, Object> map = new HashMap<>();
            map.put("data", req);
            map.put("timestamp", currentTime);

            StringEntity body = new StringEntity(JSON.toJSONString(map));
            HttpUriRequest httpUriRequest = RequestBuilder.post(getUrl(SsoConstants.LOGIN_URL))
                    .addHeader("corona_signature", md5)
                    .setEntity(body).build();
            CloseableHttpResponse response = closeableHttpClient.execute(httpUriRequest);
            return responseValid(response, new TypeReference<SSoResultDTO<SsoLoginResp>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("", "登录出错");
        }
    }

    /**
     * 获取 cosmosAccessToken
     * @param authorizationCode
     * @return
     */
    private SsoAccessTokenResp accessToken(String authorizationCode){
        SsoAccessTokenReq req = new SsoAccessTokenReq()
                .setApp_id(ssoProperty.getAppId())
                .setAuthorization_code(authorizationCode);

        Long currentTime = System.currentTimeMillis();
        String md5 = md5(Arrays.asList(ssoProperty.getAppId(), ssoProperty.getAppSecurity(), authorizationCode, req.getGrant_type(), String.valueOf(currentTime)));

        Map<String, Object> map = new HashMap<>();
        map.put("data", req);
        map.put("timestamp", currentTime);

        try {
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
            HttpUriRequest httpUriRequest = RequestBuilder.post(getUrl(SsoConstants.SYS_AUTHORIZE))
                    .addHeader("corona_signature", md5)
                    .setEntity(stringEntity)
                    .build();
            return responseValid(closeableHttpClient.execute(httpUriRequest), new TypeReference<SSoResultDTO<SsoAccessTokenResp>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 刷新token
     * @return
     */
    private SsoAccessTokenResp refreshAccessToken(String coronaRefreshToken, String cosmosRefreshToken){
        SsoAccessTokenReq req = new SsoAccessTokenReq()
                .setApp_id(ssoProperty.getAppId());

        Long currentTime = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        map.put("data", req);
        map.put("timestamp", currentTime);

        try {
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
            HttpUriRequest httpUriRequest = RequestBuilder.post(getUrl(SsoConstants.SYS_REFRESH_TOKEN))
                    .setEntity(stringEntity)
                    .addHeader("cosmosRefreshToken", coronaRefreshToken)
                    .addHeader("cosmosRefreshToken", cosmosRefreshToken)
                    .build();
            return responseValid(closeableHttpClient.execute(httpUriRequest), new TypeReference<SSoResultDTO<SsoAccessTokenResp>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当前登录信息
     * @return
     */
    private SsoAccessTokenResp accessTokenInfo(String coronaAccessToken, String cosmosRefreshToken){
        SsoAccessTokenReq req = new SsoAccessTokenReq()
                .setApp_id(ssoProperty.getAppId());

        Long currentTime = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        map.put("data", req);
        map.put("timestamp", currentTime);

        try {
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
            HttpUriRequest httpUriRequest = RequestBuilder.post(getUrl(SsoConstants.SYS_AUTHORIZE_INFO))
                    .setEntity(stringEntity)
                    .addHeader("cosmosAccessToken", coronaAccessToken)
                    .addHeader("cosmosRefreshToken", cosmosRefreshToken)
                    .build();
            return responseValid(closeableHttpClient.execute(httpUriRequest), new TypeReference<SSoResultDTO<SsoAccessTokenResp>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证登录信息
     * @return
     */
    private Boolean accessTokenValid(String coronaAccessToken, String cosmosRefreshToken){
        SsoAccessTokenReq req = new SsoAccessTokenReq()
                .setApp_id(ssoProperty.getAppId());

        Long currentTime = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        map.put("data", req);
        map.put("timestamp", currentTime);

        try {
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
            HttpUriRequest httpUriRequest = RequestBuilder.post(getUrl(SsoConstants.SYS_AUTHORIZE_VALID))
                    .setEntity(stringEntity)
                    .addHeader("cosmosAccessToken", coronaAccessToken)
                    .addHeader("cosmosRefreshToken", cosmosRefreshToken)
                    .build();
            return responseValid(closeableHttpClient.execute(httpUriRequest), new TypeReference<SSoResultDTO<Boolean>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户信息
     * @return
     */
    private SsoUserInfoResp findUserInfo(String cosmosAccessToken, String cosmosRefreshToken){
        SsoAccessTokenReq req = new SsoAccessTokenReq()
                .setApp_id(ssoProperty.getAppId());

        Long currentTime = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        map.put("data", req);
        map.put("timestamp", currentTime);

        try {
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
            HttpUriRequest httpUriRequest = RequestBuilder.post(getUrl(SsoConstants.SYS_USER_INFO))
                    .setEntity(stringEntity)
                    .addHeader("cosmosAccessToken", cosmosAccessToken)
                    .addHeader("cosmosRefreshToken", cosmosRefreshToken)
                    .build();
            return responseValid(closeableHttpClient.execute(httpUriRequest), new TypeReference<SSoResultDTO<SsoUserInfoResp>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注销登录
     * @return
     */
    private Boolean logout(String coronaAccessToken, String cosmosRefreshToken){
        SsoAccessTokenReq req = new SsoAccessTokenReq()
                .setApp_id(ssoProperty.getAppId());

        Long currentTime = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        map.put("data", req);
        map.put("timestamp", currentTime);

        try {
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
            HttpUriRequest httpUriRequest = RequestBuilder.post(getUrl(SsoConstants.SYS_LOGOUT))
                    .setEntity(stringEntity)
                    .addHeader("cosmosAccessToken", coronaAccessToken)
                    .addHeader("cosmosRefreshToken", cosmosRefreshToken)
                    .build();
            return responseValid(closeableHttpClient.execute(httpUriRequest), new TypeReference<SSoResultDTO<Boolean>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private <T> T responseValid(CloseableHttpResponse response,TypeReference<SSoResultDTO<T>> typeReference){
        if(Objects.equals(response.getStatusLine().getStatusCode(), 200)){
            try {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                SSoResultDTO<T> resultDTO = JSON.parseObject(json, typeReference);
                if(Objects.equals(resultDTO.getCode(), 1000)){
                    return resultDTO.getData();
                }else{
                    log.error("sso请求出错：{}", json);
                    throw new BizException("", "resultDTO.getMsg()");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new BizException("", "登录出错");
            }
        }else{
            throw new BizException("", "请求sso接口出错");
        }
    }


    private String md5(List<String> param){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : param) {
            stringBuilder.append(s);
        }
        String md5str = DigestUtils.md5DigestAsHex(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        log.info("MD5加密字段：{},加密后：{}", stringBuilder.toString(), md5str);
        return md5str;
    }
}
