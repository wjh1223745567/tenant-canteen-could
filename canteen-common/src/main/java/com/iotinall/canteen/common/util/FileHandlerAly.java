package com.iotinall.canteen.common.util;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.iotinall.canteen.common.util.img.ImgMeta;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
@RefreshScope
public class FileHandlerAly implements FileHandler {
    // oss 节点
    @Value("${filemanage.endpoint}")
    private String endpoint;
    // oss accessKeyId
    @Value("${filemanage.accessKeyId}")
    private String accessKeyId;
    // oss accessKeySecret
    @Value("${filemanage.accessKeySecret}")
    private String accessKeySecret;
    // oss 存储bucket
    @Value("${filemanage.bucket}")
    private String bucket;

    @Value("${filemanage.baseFile:testfile/}")
    private String mainFile;


    @Override
    public boolean deleteFile(String filePath) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。
        ossClient.deleteObject(bucket, filePath);
        // 关闭OSSClient。
        ossClient.shutdown();
        return true;
    }

    private String updateInputStream(InputStream inputStream, String name) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String key = mainFile + UUIDUtil.generateUuid() + name;
        PutObjectRequest putObjectRequest;
        putObjectRequest = new PutObjectRequest(bucket, key, inputStream);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        return key;
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
        String fullName = file.getName();
        String name = "";
        if (StringUtils.isNotBlank(fullName)) {
            name = fullName.substring(fullName.lastIndexOf("."));
        }

        return this.updateInputStream(FileUtil.getInputStream(file), name);
    }

    @Override
    public String saveFile(String type, MultipartFile multipartFile) {
        try {
            String fullName = multipartFile.getOriginalFilename();
            String name = "";
            if (StringUtils.isNotBlank(fullName)) {
                name = fullName.substring(fullName.lastIndexOf("."));
            }

            return this.updateInputStream(multipartFile.getInputStream(), name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String saveImage(String type, BufferedImage bufferedImage) {
        InputStream input = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", baos);
            input = new ByteArrayInputStream(baos.toByteArray());
            return this.updateInputStream(input, ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String saveImage(String type, BufferedImage bufferedImage, ImgMeta.ImgType imgType) {
        InputStream input = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, imgType.getName(), baos);
            input = new ByteArrayInputStream(baos.toByteArray());
            return this.updateInputStream(input, imgType.getExtName());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
