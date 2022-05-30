package com.iotinall.canteen.utils;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音
 *
 * @author loki
 * @date 2020/12/07 19:44
 */
@Slf4j
public class PinyinUtil {
    /**
     * 转全拼
     */
    public static String toFullPinyin(String data) {
        data = excludeNoChinese(data);
        if (PinyinUtil.isEnglish(data)) {
            return data;
        }

        String fullPinYin = "";
        for (int index = 0; index < data.length(); index++) {
            fullPinYin += PinyinUtil.toPinyin(String.valueOf(data.charAt(index)));
        }

        return fullPinYin;
    }

    /**
     * 转拼音首字母
     */
    public static String toFirstLetter(String data) {
        data = excludeNoChinese(data);

        if (PinyinUtil.isEnglish(data)) {
            return data;
        }

        String firstLetter = "";
        for (int index = 0; index < data.length(); index++) {
            firstLetter += PinyinUtil.toFirstChar(String.valueOf(data.charAt(index)));
        }

        return firstLetter;
    }

    /**
     * 排除非汉字
     */
    private static String excludeNoChinese(String data) {
        if (data.contains("(") || data.contains("（")) {
            if (data.contains("(")) {
                data = data.substring(0, data.indexOf("("));
            } else if (data.contains("（")) {
                data = data.substring(0, data.indexOf("（"));
            }
        }

        return data;
    }

    /**
     * 汉字转为拼音
     */
    private static String toPinyin(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

    /**
     * 获取字符串拼音的第一个字母
     */
    private static String toFirstChar(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();  //转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }
}
