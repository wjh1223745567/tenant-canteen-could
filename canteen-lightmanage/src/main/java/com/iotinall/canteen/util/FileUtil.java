package com.iotinall.canteen.util;

import cn.hutool.core.codec.Base64Decoder;
import com.iotinall.canteen.common.constant.FileConstant;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 文件处理工具类
 *
 * @author loki
 * @date 2020/12/11 10:28
 */
@Slf4j
public class FileUtil {
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
        } catch (Exception ex) {

        }
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
            urlfile = new URL(ImgPair.getFileServer() + "/" + url);
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

    /**
     * base64转成图片
     *
     * @author loki
     * @date 2020/06/22 9:53
     */
    public static File base64ToFile(String key, String imgBase64) {
        File file = null;
        try {
            file = File.createTempFile("T_" + key + "_" + System.currentTimeMillis() + "", ".jpg", FileConstant.tmpDir);
            FileOutputStream out = new FileOutputStream(file);
            out.write(Base64Decoder.decode(imgBase64));
        } catch (FileNotFoundException e) {
            log.info("生成临时图片失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
