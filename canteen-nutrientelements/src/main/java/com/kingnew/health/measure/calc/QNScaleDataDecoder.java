package com.kingnew.health.measure.calc;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Properties;

@Slf4j
public class QNScaleDataDecoder {
    static {
        log.info("共享秤路径：" + System.getProperty("user.dir") + File.separator + "scale" + File.separator + "linux" + File.separator + "algorithm_open.so");
        System.load(  System.getProperty("user.dir") + File.separator + "scale" + File.separator + "linux" + File.separator + "algorithm_open.so");
    }

    public static boolean isOSLinux() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        return os != null && os.toLowerCase().contains("scale/linux");
    }

    /**
     * 是否是linux系统
     * @return
     */
    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * generate scale measurement with source data(hexadecimal string) and user profile.
     * @param hexString 共享秤二维码内的加密字符串
     * @param height 身高，单位cm
     * @param gender 性别 0是女 1是男
     * @param age 年龄
     * @param isAthlete 是否为运动员，0为否，1为是
     * @return the scale measurement data, see detail on the document
     */
    public static native String generateBodyData(String hexString,double height,int gender, int age, int isAthlete);

}
