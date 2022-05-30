package com.iotinall.canteen.service;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.iotinall.canteen.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

/**
 * 虹软人脸识别
 *
 * @author loki
 * @date 2020/12/10 18:12
 */
@Slf4j
@Service
public class ArcSoftFaceService {
    @Value("${arc-soft.engine-path}")
    private String engineLibPath;
    @Value("${arc-soft.app-id}")
    private String APP_ID;
    @Value("${arc-soft.sdk-key}")
    private String SDK_KEY;

    private FaceEngine FACE_ENGINE = null;

    private FaceEngine getFaceEngine() {
        if (FACE_ENGINE == null) {
            FACE_ENGINE = this.initEngine();
        }

        return FACE_ENGINE;
    }

    /**
     * 身份认证
     *
     * @author loki
     * @date 2020/12/10 20:41
     */
    public Float recognition(FaceFeature faceFeature1, File target) {
        FaceEngine faceEngine = getFaceEngine();

        //人脸检测2
        FaceFeature faceFeature2 = getFaceFeature(target);
        if (faceFeature2 == null) {
            log.info("人员头像未检测到人脸");
            return 0f;
        }

        //特征比对
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature1.getFeatureData());
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(faceFeature2.getFeatureData());
        FaceSimilar faceSimilar = new FaceSimilar();

        faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        log.info("相似度：" + faceSimilar.getScore());

        return faceSimilar.getScore();
    }

    /**
     * 获取特征值
     *
     * @author loki
     * @date 2020/12/10 20:42
     */
    public FaceFeature getFaceFeature(File file) {
        FaceEngine faceEngine = getFaceEngine();

        ImageInfo imageInfo = getRGBData(file);
        List<FaceInfo> faceInfoList = new ArrayList<>();
        faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        if (CollectionUtils.isEmpty(faceInfoList)) {
            return null;
        }

        FaceFeature faceFeature = new FaceFeature();
        faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        log.info("特征值大小：" + faceFeature.getFeatureData().length);
        return faceFeature;
    }

    /**
     * 初始化引擎
     *
     * @author loki
     * @date 2020/12/10 20:40
     */
    private FaceEngine initEngine() {
        log.info("engineLibPath:{}", engineLibPath);
        log.info("app-id:{}", APP_ID);
        log.info("app-sdk-key:{}", SDK_KEY);
        FaceEngine faceEngine = new FaceEngine(engineLibPath);

        //激活引擎
        int errorCode = faceEngine.activeOnline(APP_ID, SDK_KEY);

        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.info("错误码：{}", errorCode);
            throw new BizException("", "引擎激活失败");
        }

        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            throw new BizException("", "获取激活文件信息失败");
        }

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);

        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new BizException("", "初始化引擎失败");
        }

        return faceEngine;
    }
}
