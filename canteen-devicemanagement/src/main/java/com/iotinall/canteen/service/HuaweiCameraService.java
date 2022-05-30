package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constants.HuaweiErrorCodeEnum;
import com.iotinall.canteen.dto.huawei.*;
import com.iotinall.canteen.entity.DeviceHuaweiCamera;
import com.iotinall.canteen.property.HuaweiProperty;
import com.iotinall.canteen.repository.HuaweiCameraRepository;
import com.iotinall.canteen.utils.DateTimeFormatters;
import com.iotinall.canteen.utils.DateUtil;
import com.iotinall.canteen.utils.HttpClientUtil;
import io.undertow.util.Headers;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 华为摄像头对接
 *
 * @author loki
 * @date 2021/6/28 19:36
 **/
@Slf4j
@Service
public class HuaweiCameraService {
    @Resource
    private HuaweiProperty huaweiProperty;
    @Resource
    private HuaweiCameraRepository huaweiCameraRepository;

    public static final String API_LOGIN = "/loginInfo/login/v1.0";
    public static final String API_DOMAIN = "/device/domainRoute/v1.1";
    public static final String API_CAMERA_LIST = "/device/deviceList/v1.0";
    public static final String API_SNAPSHOT = "/platform/platformSnapshot/%s/%s";
    public static final String API_SNAPSHOT_PIC = "/platform/snapshotlist/%s/%s/%s/%s/%d/%d/%d";

    private static final String HEADER_KEY_COOKIE = "Set-Cookie";
    private static final String HEADER_COOKIE_NAME = "Cookie";
    private static final Integer DEFAULT_QUERY_FROM_INDEX = 1;
    private static final Integer DEFAULT_QUERY_TO_INDEX = 5;

    static CloseableHttpClient closeableHttpClient;

    static {
        closeableHttpClient = HttpClientUtil.createSSLClientDefault();
    }

    /**
     * 获取所有摄像头
     *
     * @author loki
     * @date 2021/7/9 11:32
     **/
    public Object all(String keywords) {
        SpecificationBuilder builder =
                SpecificationBuilder.builder()
                        .whereByOr(Criterion.like("code", keywords),
                                Criterion.like("code", keywords));

        List<DeviceHuaweiCamera> cameraList = this.huaweiCameraRepository.findAll(builder.build());
        if (CollectionUtils.isEmpty(cameraList)) {
            return Collections.EMPTY_SET;
        }

        List<CameraDTO> cameraDTOList = new ArrayList<>();
        CameraDTO cameraDTO;
        for (DeviceHuaweiCamera camera : cameraList) {
            cameraDTO = new CameraDTO();
            cameraDTOList.add(cameraDTO);
            cameraDTO.setCode(camera.getCode());
            cameraDTO.setName(camera.getName());
        }
        return cameraDTOList;
    }


    /**
     * 组装请求uri
     *
     * @author loki
     * @date 2021/6/29 14:59
     **/
    private String buildUri(String api) {
        return "https://" + huaweiProperty.getIp() + ":" + huaweiProperty.getPort() + api;
    }

    /**
     * 登入
     *
     * @author loki
     * @date 2021/6/28 19:38
     **/
    public String login() {
        LoginReq req = new LoginReq()
                .setUserName(huaweiProperty.getUsername())
                .setPassword(huaweiProperty.getPassword());
        try {
            StringEntity body = new StringEntity(JSON.toJSONString(req));
            HttpUriRequest httpUriRequest = RequestBuilder.post(buildUri(API_LOGIN))
                    .addHeader(Headers.CONTENT_TYPE_STRING, MediaType.APPLICATION_JSON_VALUE)
                    .setEntity(body)
                    .build();

            CloseableHttpResponse response = request(httpUriRequest);

            LoginResp resp = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"),
                    new TypeReference<LoginResp>() {
                    });

            Integer code = resp.getResultCode();
            if (code != HuaweiErrorCodeEnum.SUCCESS_0.getCode()
                    && code != HuaweiErrorCodeEnum.ERR_UPDATE_PWD_NEEDED.getCode()
                    && code != HuaweiErrorCodeEnum.ERR_PWD_EXPIRE.getCode()) {
                log.info("请求错误，错误码：{}，错误提示：{}", resp.getResultCode(), HuaweiErrorCodeEnum.getByCode(resp.getResultCode()));
                throw new BizException("请求错误");
            }

            //获取请求头参数
            Header header = response.getFirstHeader(HEADER_KEY_COOKIE);

            return header.getValue().split(";")[0];

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取摄像头区域
     *
     * @author loki
     * @date 2021/6/28 19:55
     **/
    public void getDomain() {
    }

    /**
     * 获取摄像头列表保存到本地
     *
     * @author loki
     * @date 2021/6/28 20:01
     **/
    public void getCameraList() {
        getCameraList(DEFAULT_QUERY_FROM_INDEX, DEFAULT_QUERY_TO_INDEX);
    }

    /**
     * 获取摄像头列表保存到本地
     *
     * @author loki
     * @date 2021/6/28 20:01
     **/
    private void getCameraList(Integer fromIndex, Integer toIndex) {
        CameraBriefInfoV2 cameraInfo = getCameraList(fromIndex + "", toIndex + "");
        if (null == cameraInfo) {
            return;
        }

        List<CameraBriefInfo> cameraList;
        Integer total = cameraInfo.getTotal();
        if (total > 0 && null != cameraInfo.getCameraBriefInfoList()) {
            //保存或者更新摄像机到本地
            cameraList = cameraInfo.getCameraBriefInfoList().getCameraBriefInfo();
            DeviceHuaweiCamera huaweiCamera;
            List<DeviceHuaweiCamera> huaweiCameraList = new ArrayList<>(cameraList.size());
            for (CameraBriefInfo camera : cameraList) {
                huaweiCamera = this.huaweiCameraRepository.findByCode(camera.getCode());
                if (null == huaweiCamera) {
                    huaweiCamera = new DeviceHuaweiCamera();
                }
                huaweiCameraList.add(huaweiCamera);
                BeanUtils.copyProperties(camera, huaweiCamera);
            }

            huaweiCameraRepository.saveAll(huaweiCameraList);

            if (total > cameraInfo.getIndexRange().getToIndex()) {
                getCameraList(cameraInfo.getIndexRange().getToIndex() + 1, cameraInfo.getIndexRange().getToIndex() + DEFAULT_QUERY_TO_INDEX);
            }
        }
    }

    /**
     * 获取摄像头列表
     *
     * @author loki
     * @date 2021/6/29 20:36
     **/
    private CameraBriefInfoV2 getCameraList(String fromIndex, String toIndex) {
        HttpUriRequest httpUriRequest = RequestBuilder.get(buildUri(API_CAMERA_LIST))
                .addParameter("deviceType", "35")
                .addParameter("fromIndex", fromIndex)
                .addParameter("toIndex", toIndex)
                .addHeader(Headers.CONTENT_TYPE_STRING, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .addHeader(HEADER_COOKIE_NAME, login())
                .build();

        try {
            DeviceListResp resp = JSON.parseObject(EntityUtils.toString(request(httpUriRequest).getEntity(), "UTF-8"),
                    new TypeReference<DeviceListResp>() {
                    });

            Integer code = resp.getResultCode();
            if (code != HuaweiErrorCodeEnum.SUCCESS_0.getCode()) {
                log.info("请求错误，错误码：{}，错误提示：{}", resp.getResultCode(), HuaweiErrorCodeEnum.getByCode(resp.getResultCode()));
                throw new BizException("请求错误");
            }

            return resp.getCameraBriefInfosV2();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 发起请求
     *
     * @author loki
     * @date 2021/6/30 17:04
     **/
    private CloseableHttpResponse request(HttpUriRequest httpUriRequest) {
        try {
            CloseableHttpResponse response = closeableHttpClient.execute(httpUriRequest);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new BizException("请求失败");
            }

            return response;
        } catch (Exception ex) {
            throw new BizException("请求错误");
        }
    }

    /**
     * 抓拍
     *
     * @author loki
     * @date 2021/6/30 16:53
     **/
    public void snapshot(String cameraCode, String domainCode) {
        HttpUriRequest httpUriRequest = RequestBuilder.get(buildUri(String.format(API_SNAPSHOT, cameraCode, domainCode)))
                .addHeader(Headers.CONTENT_TYPE_STRING, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .addHeader(HEADER_COOKIE_NAME, login())
                .build();

        try {
            HuaweiApiResp resp = JSON.parseObject(EntityUtils.toString(request(httpUriRequest).getEntity(), "UTF-8"),
                    new TypeReference<HuaweiApiResp>() {
                    });

            Integer code = resp.getResultCode();
            if (code != HuaweiErrorCodeEnum.SUCCESS_0.getCode()) {
                log.info("请求错误，错误码：{}，错误提示：{}", resp.getResultCode(), HuaweiErrorCodeEnum.getByCode(resp.getResultCode()));
                throw new BizException("请求错误");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取摄像头图片
     *
     * @author loki
     * @date 2021/6/28 20:02
     **/
    public void getSnapshotList(String cameraCode, String domainCode) {
        LocalDateTime beginTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        getSnapshotPicList(cameraCode, domainCode, beginTime, endTime);
    }

    /**
     * 获取摄像头图片
     *
     * @author loki
     * @date 2021/6/28 20:02
     **/
    public void getSnapshotPicList(String cameraCode, String domainCode, LocalDateTime beginTime, LocalDateTime endTime) {
        Integer fromIndex = 1;
        Integer toIndex = 10;
        Integer snapType = 4;

        HttpUriRequest httpUriRequest = RequestBuilder.get(
                buildUri(
                        String.format(
                                API_SNAPSHOT_PIC,
                                cameraCode,
                                domainCode,
                                DateUtil.localDatetime2Str(beginTime, DateTimeFormatters.YYYYMMDDHHMMSS),
                                DateUtil.localDatetime2Str(endTime, DateTimeFormatters.YYYYMMDDHHMMSS),
                                fromIndex,
                                toIndex,
                                snapType)))
                .addHeader(Headers.CONTENT_TYPE_STRING, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .addHeader(HEADER_COOKIE_NAME, login())
                .build();

        try {
            CameraSnapshotPicResp resp = JSON.parseObject(EntityUtils.toString(request(httpUriRequest).getEntity(), "UTF-8"),
                    new TypeReference<CameraSnapshotPicResp>() {
                    });

            Integer code = resp.getResultCode();
            if (code != HuaweiErrorCodeEnum.SUCCESS_0.getCode()) {
                log.info("请求错误，错误码：{}，错误提示：{}", resp.getResultCode(), HuaweiErrorCodeEnum.getByCode(resp.getResultCode()));
                throw new BizException("请求错误");
            }

            if (resp.getSnapshotInfoList().getTotal() > 0) {

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
