package com.iotinall.canteen.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author loki
 * @date 2019-09-25 11:12 上午
 **/
public class UUIDUtil {

    public static String[] chars = new String[]{"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9"};

    /**
     * 生成八位
     *
     * @author loki
     * @date 2019/10/26 17:08
     */
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            Random random = new Random();
            shortBuffer.append(chars[random.nextInt(10)]);
        }
        return shortBuffer.toString();
    }

    /**
     * 生成自定义长度
     *
     * @author loki
     * @date 2019/10/26 17:08
     */
    public static String generateUuid(Integer length) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < length; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 生成16位
     *
     * @author loki
     * @date 2019/10/26 17:08
     */
    public static String generateUuid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    /**
     * 外带外购商品编号
     * @return
     */
    public static String takeoutProductOrder(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
