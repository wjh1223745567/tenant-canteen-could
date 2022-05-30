package com.iotinall.canteen.service;

import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class KitchenCameraImgService {

    public boolean timeCompare(LocalDateTime timeOne, LocalDateTime timeTwo) {
        if (timeOne.toLocalDate().isEqual(timeTwo.toLocalDate())
                && timeOne.getHour() == timeTwo.getHour()
                && timeOne.getMinute() == timeTwo.getMinute()) {
            return true;
        } else {
            return false;
        }
    }

    @Resource
    private FileHandler fileHandler;

    public String upCameraImg(String url) {

        File file = FileUtil.downloadImage(url);
        if (!file.exists()) {
            return null;
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            return fileHandler.saveImage("group1", bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }

        return null;
    }
}
