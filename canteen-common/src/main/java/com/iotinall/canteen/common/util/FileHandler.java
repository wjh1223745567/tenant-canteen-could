package com.iotinall.canteen.common.util;

import com.iotinall.canteen.common.util.img.ImgMeta;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;

public interface FileHandler {

    /**
     * 删除文件
     *
     * @param filePath 文件是
     * @return
     */
    boolean deleteFile(String filePath);

    /**
     * 保存文件
     *
     * @param type          上一级路径
     * @param multipartFile 文件
     * @return 返回文件名
     */
    String saveFile(String type, MultipartFile multipartFile);

    /**
     * 保存文件
     *
     * @param type          上一级路径
     * @param file 文件
     * @return 返回文件名
     */
    String saveFile(String type, File file);

    /**
     * 保存图片
     *
     * @param type          文件类型
     * @param bufferedImage 要保存的文件
     * @return
     */
    String saveImage(String type, BufferedImage bufferedImage);

    /**
     * 保存图片
     *
     * @param type          文件类型
     * @param bufferedImage 要保存的文件
     * @param imgType       该图片旋转的角度
     * @return
     */
    String saveImage(String type, BufferedImage bufferedImage, ImgMeta.ImgType imgType);

}
