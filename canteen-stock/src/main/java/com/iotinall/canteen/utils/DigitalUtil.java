package com.iotinall.canteen.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author loki
 * @date 2020/04/29 14:06
 */
public class DigitalUtil {
    private static final String SYMBOLS = "0123456789";

    /**
     * 生成随机验证码
     *
     * @author loki
     * @date 2020/04/29 14:06
     */
    public static String genCheckCode(Integer len) {
        Random RANDOM = new SecureRandom();
        char[] nonceChars = new char[len];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }

        return new String(nonceChars);
    }
}
