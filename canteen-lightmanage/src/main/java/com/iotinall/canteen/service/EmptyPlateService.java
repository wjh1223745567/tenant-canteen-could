package com.iotinall.canteen.service;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.constant.FileConstant;
import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.devicemanagement.FeignCameraDto;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateAlarmReq;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateAlarmResult;
import com.iotinall.canteen.dto.tcpclient.TcpMsgDTO;
import com.iotinall.canteen.entity.EmptyPlateImgRecord;
import com.iotinall.canteen.entity.EmptyPlateMsgConfig;
import com.iotinall.canteen.repository.EmptyPlateAnalysisRecordRepository;
import com.iotinall.canteen.repository.EmptyPlateImgRecordRepository;
import com.iotinall.canteen.repository.EmptyPlateMsgConfigRepository;
import com.iotinall.canteen.util.EmptyPlateUtil;
import com.iotinall.canteen.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 光盘行动处理类
 * 1、设备调用接口上报数据
 * 2、图片存于FastDFS
 *
 * @author loki
 * @date 2021/7/6 13:55
 **/
@Slf4j
@Service(value = "emptyPlateService")
public class EmptyPlateService {
    @Resource
    private EmptyPlateImgRecordRepository emptyPlateImgRecordRepository;
    @Resource
    private EmptyPlateMsgConfigRepository emptyPlateMsgConfigRepository;
    @Resource
    private EmptyPlateAnalysisRecordRepository emptyPlateRecognitionRecordRepository;
    @Resource
    private ArcSoftFaceService arcSoftFaceService;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignEquFaceDeviceService feignEquFaceDeviceService;
    @Resource
    private FeignTcpClientService feignTcpClientService;
    @Resource
    private FileHandler fileHandler;

    @Value("${empty-plate.limit.left:0.5264403736591339}")
    private Double limitLeft;
    @Value("${empty-plate.limit.right:0.5264403736591339}")
    private Double limitRight;
    @Value("${empty-plate.retention-days:15}")
    private Integer retentionDays;

    /**
     * 1、描框
     * 3、发送路径到指定的显示器
     */
    @Async
    public void alarm(EmptyPlateAlarmReq req) {
        //摄像头
        FeignCameraDto camera = feignEquFaceDeviceService.findByDeviceNo(req.getDevice_sn(), AuthService.token);
        if (null == camera) {
            log.info("摄像头未配置");
            return;
        }

        //是否存在违规
        List<EmptyPlateAlarmResult> violateList = getViolationPosition(req);
        if (CollectionUtils.isEmpty(violateList)) {
            //return;
        }

        File file = FileUtil.base64ToFile(req.getDevice_sn(), req.getBase64_img());
        if (file != null) {

            //保存原图
            String originalFileUrl = fileHandler.saveFile("group1", file);

            //描框
            File fileFrame = drawFrame(file, violateList);

            if (null != fileFrame) {
                String frameImgUrl = fileHandler.saveFile("group1", fileFrame);

                //保存上传记录
                try {
                    EmptyPlateImgRecord emptyPlateImgRecord = new EmptyPlateImgRecord();
                    emptyPlateImgRecord.setDeviceSn(req.getDevice_sn());
                    emptyPlateImgRecord.setDeviceName(req.getDevice_name());
                    emptyPlateImgRecord.setOriginalImgUrl(originalFileUrl);
                    emptyPlateImgRecord.setFrameImgUrl(frameImgUrl);
                    emptyPlateImgRecord.setCoordinate(JSON.toJSONString(violateList));
                    emptyPlateImgRecord.setAnalysis(0);
                    emptyPlateImgRecord.setTenantOrgId(camera.getTenantOrgId());
                    this.emptyPlateImgRecordRepository.save(emptyPlateImgRecord);
                } catch (Exception ex) {
                    log.info("保存上传记录失败");
                }

                //推送到显示屏
                List<EmptyPlateMsgConfig> configList = this.emptyPlateMsgConfigRepository.findByCameraId(camera.getId());
                if (!CollectionUtils.isEmpty(configList)) {
                    feignTcpClientService.send(new TcpMsgDTO()
                                    .setData(ImgPair.getFileServer() + frameImgUrl)
                                    .setClientIds(configList.stream().map(EmptyPlateMsgConfig::getClientId).collect(Collectors.toList()))
                            , AuthService.token);
                }
                fileFrame.delete();
            }
            file.delete();
        }
    }

    /**
     * 获取图片上违规的点
     *
     * @author loki
     * @date 2021/04/28 10:11
     */
    private List<EmptyPlateAlarmResult> getViolationPosition(EmptyPlateAlarmReq req) {
        if (StringUtil.isBlank(req.getDevice_sn())
                || CollectionUtils.isEmpty(req.getResult())) {
            return Collections.EMPTY_LIST;
        }

        /**
         * 1、AIbox标记图片违规
         * 2、排除走廊坐标
         */
        List<EmptyPlateAlarmResult> resultList = new ArrayList<>();
        for (EmptyPlateAlarmResult r : req.getResult()) {
            if (!r.getLabel_name().equals("no")) {
                continue;
            }

            resultList.add(r);
        }

        return resultList;
    }

    /**
     * 描框
     *
     * @author loki
     * @date 2021/04/16 15:11
     */
    private File drawFrame(File file, List<EmptyPlateAlarmResult> resultList) {
        if (null == file || CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        BufferedImage image;
        File fileWithFrame;
        try {
            image = ImageIO.read(file);
            if (null == image) {
                return null;
            }

            Integer width = image.getWidth();
            Integer height = image.getHeight();

            Thumbnails.Builder<File> R = Thumbnails.of(file).scale(1f).outputFormat("jpg");

            for (EmptyPlateAlarmResult result : resultList) {
                if (null == result.getX0()
                        || null == result.getX1()
                        || null == result.getY0()
                        || null == result.getY1()) {
                    continue;
                }

                int X0 = EmptyPlateUtil.getPoint(result.getX0(), width);
                int X1 = EmptyPlateUtil.getPoint(result.getX1(), width);
                int Y0 = EmptyPlateUtil.getPoint(result.getY0(), height);
                int Y1 = EmptyPlateUtil.getPoint(result.getY1(), height);

                int waterMarkWidth = X1 - X0;
                int waterMarkHeight = Y1 - Y0;

//                log.info("检测结果:{}", result.getLabel_name());
//                log.info("X0:{}", X0);
//                log.info("X1:{}", X1);
//                log.info("Y0:{}", Y0);
//                log.info("Y1:{}", Y1);
//                log.info("照片尺寸:{}", width + "-" + height);
//                log.info("水印尺寸:{}", waterMarkWidth + "-" + waterMarkHeight);

                R.watermark((int enclosingWidth,
                             int enclosingHeight,
                             int w,
                             int h,
                             int insetLeft,
                             int insetRight,
                             int insetTop,
                             int insetBottom) -> new Point(X0, Y0),
                        createWaterMarkImage(waterMarkWidth, waterMarkHeight, getColor(result.getLabel_name())), 0.65f);
            }

            //保存绘制后的新图片
            fileWithFrame = File.createTempFile("F" + System.currentTimeMillis(), ".jpg", FileConstant.tmpDir);
            R.toFile(fileWithFrame);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("描点失败");
            return null;
        }
        return fileWithFrame;
    }

    /**
     * 获取颜色
     *
     * @author loki
     * @date 2021/04/17 16:10
     */
    private Color getColor(String checkResult) {
        return checkResult.equals("ok") ?
                new Color(83, 117, 180)
                : new Color(144, 112, 185);
    }

    /**
     * 生成水印图片
     *
     * @author loki
     * @date 2021/04/17 14:49
     */
    private BufferedImage createWaterMarkImage(Integer width, Integer height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return image;
    }

    /**
     * 定时任务分析违规记录
     *
     * @author loki
     * @date 2021/7/6 14:43
     **/
    public void analysis() {
        //所有未人脸比对的违规记录
        List<EmptyPlateImgRecord> notAnalysisRecordList = emptyPlateImgRecordRepository.queryAllRecordNotAnalysis();
        if (CollectionUtils.isEmpty(notAnalysisRecordList)) {
            return;
        }

        log.info("待分析的违规数量：{}", notAnalysisRecordList.size());
        for (EmptyPlateImgRecord record : notAnalysisRecordList) {
            try {
                String originalFilePath = record.getOriginalImgUrl();
                File originalFile = new File(originalFilePath);

                List<EmptyPlateAlarmResult> coordinateList = JSON.parseArray(record.getCoordinate(), EmptyPlateAlarmResult.class);

                if (!originalFile.exists() || CollectionUtils.isEmpty(coordinateList)) {
                    record.setAnalysis(3);
                } else {
                    log.info("开始裁剪");
                    List<File> violatorsImgFile = cropPicture(originalFile, coordinateList);
                    log.info("裁剪的人员数：{}", violatorsImgFile.size());
                    if (!CollectionUtils.isEmpty(violatorsImgFile)) {
                        log.info("开始分析。。。");
                        analysis(record, violatorsImgFile);
                        record.setAnalysis(1);
                        log.info("分析结束。。。");
                    } else {
                        log.info("照片中不存在违规人员");
                        record.setAnalysis(4);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                record.setAnalysis(2);
            }
        }

        this.emptyPlateImgRecordRepository.saveAll(notAnalysisRecordList);
    }

    /**
     * 裁剪图片
     *
     * @author loki
     * @date 2021/05/25 16:52
     */
    private List<File> cropPicture(File originalFile, List<EmptyPlateAlarmResult> coordinateList) {
        Integer width, height;
        try {
            BufferedImage image = ImageIO.read(originalFile);
            width = image.getWidth();
            height = image.getHeight();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        List<File> violatorPictureList = new ArrayList<>(coordinateList.size());
        int index = 1;
        File tailoringFile;
        for (EmptyPlateAlarmResult coordinate : coordinateList) {
            try {
                tailoringFile = getTailoringFile(originalFile.getName(), index);
                if (null == tailoringFile || !tailoringFile.exists()) {
                    continue;
                }

                int X0 = EmptyPlateUtil.getPoint(coordinate.getX0(), width);
                int X1 = EmptyPlateUtil.getPoint(coordinate.getX1(), width);
                int Y0 = EmptyPlateUtil.getPoint(coordinate.getY0(), height);
                int Y1 = EmptyPlateUtil.getPoint(coordinate.getY1(), height);
                int XW = X1 - X0;
                int YH = Y1 - Y0;

//                log.info("长：{}", width);
//                log.info("宽:{}", height);
//                log.info("X0:{}", X0);
//                log.info("X1:{}", X1);
//                log.info("Y0:{}", Y0);
//                log.info("Y1:{}", Y1);
//                log.info("XW:{}", XW);
//                log.info("YH:{}", YH);

                Thumbnails.of(originalFile).sourceRegion(
                        X0,
                        Y0,
                        XW,
                        YH
                ).size(XW, YH).toFile(tailoringFile);

                violatorPictureList.add(tailoringFile);
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }

            index++;
        }

        return violatorPictureList;
    }

    /**
     * 获取裁剪的文件名称
     *
     * @author loki
     * @date 2021/7/8 11:12
     **/
    private File getTailoringFile(String originalFileName, int index) {
        String fileName = originalFileName.replace(".jpg", "") + "_" + index;

        try {
            return File.createTempFile(fileName, ".jpg", FileConstant.tmpDir);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 识别
     *
     * @author loki
     * @date 2021/7/8 11:46
     **/
    private void analysis(EmptyPlateImgRecord record, List<File> violateFileList) {
        //获取所有的人员向量

    }
}