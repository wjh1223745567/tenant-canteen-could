package com.iotinall.canteen.utils;
import com.iotinall.canteen.common.constant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * 文件处理工具类
 *
 * @author loki
 * @date 2020/12/11 10:28
 */
@Slf4j
public class FileUtil {
    public static final String IMG_TYPE_PNG = ".png";

    /**
     * @author loki
     * @date 2021/03/01 16:03
     */
    public static BufferedImage base64ToBufferedImage(String base64) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Utils.decodeFromString(base64))) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * base64转成图片
     *
     * @author loki
     * @date 2020/06/22 9:53
     */
    public static File base64ToTempFile(String imgBase64, String fileName) {
        File file = null;
        try {
            file = File.createTempFile("temp_file", fileName + ".png", FileConstant.tmpDir);
            FileOutputStream out = new FileOutputStream(file);
            out.write(Base64.getDecoder().decode(imgBase64));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    /**
     * base64转成图片
     *
     * @author loki
     * @date 2020/06/22 9:53
     */
    public static File base64ToFile(String imgBase64, String path, String fileName) {
        String filePath = path + fileName;
        File file = new File(filePath);
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(Base64.getDecoder().decode(imgBase64));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static BufferedImage getFileBuffer(MultipartFile file) {
        try {
            return ImageIO.read(Objects.requireNonNull(multipartFileToFile(file)));
        } catch (Exception ex) {

        }

        return null;
    }

    /**
     * multipartfile 转成 file
     *
     * @author loki
     * @date 2020/12/10 19:18
     */
    public static File multipartFileToFile(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("temp_file", file.getOriginalFilename(), FileConstant.tmpDir);
            file.transferTo(tempFile);
            return tempFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 将url 转换成base64
     *
     * @author loki
     * @date 2021/01/13 10:30
     */
    public static String getBase64FromUrl(String url) {
        File file = getFileFormUrl(url);
        String filebase64 = fileToBase64(file);
        if (file != null) {
            file.delete();
        }
        return filebase64;
    }

    public static String fileToBase64(File file) {
        try (FileInputStream inputFile = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            return new String(Base64.getEncoder().encode(buffer), StandardCharsets.UTF_8);
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * @author loki
     * @date 2021/01/13 10:15
     */
    public static File getFileFormUrl(String url) {
        String fileName = url.substring(url.lastIndexOf("."));
        File file = null;
        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("net_url", fileName, FileConstant.tmpDir);
            urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);

            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File downloadImage(String url) {
        String fileName = url.substring(url.lastIndexOf("."));
        File file = null;

        URL urlfile;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {

            file = File.createTempFile("camera_img", fileName, FileConstant.tmpDir);
            urlfile = new URL("https://iotinall.oss-cn-shenzhen.aliyuncs.com/" + url);
            inputStream = urlfile.openStream();
            outputStream = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {

                outputStream.write(buffer, 0, bytesRead);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;

    }
}
