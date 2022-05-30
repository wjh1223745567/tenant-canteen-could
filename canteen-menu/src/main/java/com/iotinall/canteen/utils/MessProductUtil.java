package com.iotinall.canteen.utils;

import java.util.HashMap;

/**
 * 产品工具
 */
public class MessProductUtil {
    private static final int MAX_LEN = 4;
    private static final HashMap<Integer, String> catalogCache = new HashMap<>();

    /**
     * 构建catalogString
     *
     * @param catalog
     * @return
     */
    public static String buildCatalogStr(int catalog) {
        String s1 = catalogCache.get(catalog);
        if (s1 != null) {
            return s1;
        } else {
            String s = Integer.toBinaryString(catalog);
            if (s.length() < MAX_LEN) {
                return fill(s, "0", MAX_LEN - s.length());
            }
            return s;
        }
    }

    /**
     * 构建查询语句
     *
     * @param catalog
     * @return
     */
    public static String buildCatalogLikeStr(Integer catalog) {
        if(catalog == null){
            return null;
        }
        return buildCatalogStr(catalog).replaceAll("0", "_");
    }

    private static String fill(String value, String fillStr, int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(fillStr);
        }
        return sb.append(value).toString();
    }
}
