package com.iotinall.canteen.common.util.img;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author w05811
 * @Description
 * @Date 2018/10/15 16:38
 */
public class ImageUtils {

    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 获取图片流
     *
     * @param strUrl
     * @return
     * @throws Exception
     */
    public static byte[] getImageFromNetByUrl(String strUrl) throws Exception {
        byte[] btImg = null;
        //转换ip和端口
        int iIpStart = strUrl.indexOf("http://");
        if (-1 == iIpStart) {
            String strTemp = "http://";
            //+ officeVehicleConfig.getStrIPPort() + strUrl;
            strUrl = strTemp + strUrl;
        }
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        //File fie = new File("");
        //通过输入流获取图片数据
        InputStream inStream = null;
        try {
            inStream = conn.getInputStream();
            //得到图片的二进制数据
            //String str = inStream.toString();
            btImg = readInputStream(inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        return btImg;
    }

    /**
     * 根据文件路径获得数据的字节流
     *
     * @param imgFilePath 本地路径
     * @return
     */
    public static byte[] getImageBytesByFilePath(String imgFilePath) throws Exception {
        byte[] data = null;
        // 读取图片字节数组
        InputStream in = null;
        try {
            in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
        } finally {
            if (null != in) {
                in.close();
            }
        }
        return data;
    }

    /**
     * 从输入流中获取数据的字节流
     *
     * @param inStream 输入流
     * @return byte[] 字节流
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 图片转base64编码,返回String
     * 用于减少HTTP请求，适用于10K以下图片
     *
     * @param imgByte
     * @return
     * @throws Exception
     */
    public static String getBase64String(byte[] imgByte) {
        return Base64.encodeBase64String(imgByte);
    }

    /**
     * 图片转base64编码,返回byte数组
     *
     * @param imgByte
     * @return
     * @throws Exception
     */
    public static byte[] getBase64(byte[] imgByte) {
        return Base64.encodeBase64(imgByte);
    }

    /**
     * base64字符串转byte[]
     *
     * @param base64Str
     * @return
     */
    public static byte[] base64String2Byte(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }


    /**
     * @param path 本地转化后的JPG文件地址
     * @return 本地转化后的JPG文件字节流数组
     */
    private static byte[] getIoFileToByte(String path) {
        File file = new File(path);
        byte[] jpgBytes = new byte[(int) file.length()];
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            byte[] bytes = new byte[2048];
            int hasNext;
            hasNext = is.read(bytes);
            while (hasNext != -1) {
                bytestream.write(bytes, 0, hasNext);
                hasNext = is.read(bytes);
            }
            jpgBytes = bytestream.toByteArray();
            is.close();
        } catch (IOException e) {
            logger.debug("read localJPG is failed:{}", e.getMessage());
        }
        return jpgBytes;
    }

}
