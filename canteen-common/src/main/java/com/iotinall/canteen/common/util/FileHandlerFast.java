package com.iotinall.canteen.common.util;

import cn.hutool.core.io.FileUtil;
import com.iotinall.canteen.common.util.img.ImgMeta;
import org.csource.fastdfs.client.FastdfsClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xin-bing
 * @date 10/28/2019 15:11
 */
@Service
@Primary
public class FileHandlerFast implements FileHandler {
    @Resource
    private FastdfsClient fastdfsClient;

    private DateTimeFormatter YYYYMMDDHHMM5S = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    // 获取后缀名
    private String getExtName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        int index;
        if (originalFilename != null && (index = originalFilename.lastIndexOf(".")) != -1) {
            return originalFilename.substring(index + 1);
        }
        return "";
    }

    // 获取文件路径
    private String getFilePath(String type, String extName) {
        String fileName = LocalDateTime.now().format(YYYYMMDDHHMM5S);
        StringBuilder filePath = new StringBuilder(type).append(File.separator).append(fileName);
        if (!extName.isEmpty()) {
            filePath.append(".").append(extName);
        }
        return filePath.toString();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件是
     * @return
     */
    @Override
    public boolean deleteFile(String filePath) {
        return fastdfsClient.delete(filePath);
    }

    /**
     * 保存文件
     *
     * @param type          上一级路径
     * @param multipartFile 文件
     * @return 返回文件名
     */
    @Override
    public String saveFile(String type, MultipartFile multipartFile) {
        try {
            return fastdfsClient.upload(multipartFile.getBytes(), multipartFile.getOriginalFilename(), type);
        } catch (Exception e) {
            throw new RuntimeException("save file failed:", e);
        }
    }

    /**
     * 保存文件
     *
     * @param type 上一级路径
     * @param file 文件
     * @return 返回文件名
     */
    @Override
    public String saveFile(String type, File file) {
        try {
            return fastdfsClient.upload(FileUtil.readBytes(file), file.getName(), type);
        } catch (Exception e) {
            throw new RuntimeException("save file failed:", e);
        }
    }

    /**
     * 保存图片
     *
     * @param type          文件类型
     * @param bufferedImage 要保存的文件
     * @return
     */
    @Override
    public String saveImage(String type, BufferedImage bufferedImage) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", baos);
            return fastdfsClient.upload(baos.toByteArray(), System.currentTimeMillis() + ".jpg", type);
        } catch (Exception e) {
            throw new RuntimeException("saveImage error", e);
        }
    }

    /**
     * 保存图片
     *
     * @param type          文件类型
     * @param bufferedImage 要保存的文件
     * @param imgType       该图片旋转的角度
     * @return
     */
    @Override
    public String saveImage(String type, BufferedImage bufferedImage, ImgMeta.ImgType imgType) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, imgType.getName(), baos);
            return fastdfsClient.upload(baos.toByteArray(), imgType.getExtName(), type);
        } catch (Exception e) {
            throw new RuntimeException("saveImage error", e);
        }
    }

}
