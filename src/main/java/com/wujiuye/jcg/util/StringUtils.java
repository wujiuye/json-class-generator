package com.wujiuye.jcg.util;

/**
 * 字符串工具类
 *
 * @author wujiuye 2020/06/09
 */
public class StringUtils {

    /**
     * 将字符串首字母改为大写
     *
     * @param str
     * @return
     */
    public static String fistCharToUpperCase(String str) {
        char fistChar = str.charAt(0);
        if (fistChar >= 'A' && fistChar <= 'Z') {
            return str;
        }
        fistChar = (char) (fistChar + ('A' - 'a'));
        return fistChar + str.substring(1);
    }

}
