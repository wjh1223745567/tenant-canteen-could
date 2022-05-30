package com.iotinall.canteen.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

/**
 * @author bingo
 * @date 11/26/2019 20:43
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 15位身份证号
     */
    private static final Integer FIFTEEN_ID_CARD = 15;
    /**
     * 18位身份证号
     */
    private static final Integer EIGHTEEN_ID_CARD = 18;


    /**
     * 从身份证获取生日
     *
     * @param idCard 身份证号码
     * @return LocalDate
     */
    public static LocalDate getBirthday(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return null;
        }
        String str;
        if (idCard.length() == 18) {
            str = idCard.substring(6, 14);
        } else if (idCard.length() == 15) {
            str = "19" + idCard.substring(6, 12);
        } else {
            throw new IllegalArgumentException("illegal id card:" + idCard);
        }
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 根据身份证号获取性别
     *
     * @param IDCard
     * @return
     */
    public static Integer getGender(String IDCard) {
        Integer sex = 2;
        if (StringUtils.isNotBlank(IDCard)) {
            //15位身份证号
            if (IDCard.length() == FIFTEEN_ID_CARD) {
                if (Integer.parseInt(IDCard.substring(14, 15)) % 2 == 0) {
                    sex = 0;
                } else {
                    sex = 1;
                }
                //18位身份证号
            } else if (IDCard.length() == EIGHTEEN_ID_CARD) {
                // 判断性别
                if (Integer.parseInt(IDCard.substring(16).substring(0, 1)) % 2 == 0) {
                    sex = 0;
                } else {
                    sex = 1;
                }
            }
        }
        return sex;
    }
}
