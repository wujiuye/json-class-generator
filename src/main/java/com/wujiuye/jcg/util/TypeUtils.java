package com.wujiuye.jcg.util;

/**
 * 类型工具
 *
 * @author wujiuye 2020/06/08
 */
public class TypeUtils {

    public static String getInternalName(String className) {
        return className.replace(".", "/");
    }

    public static String getSignature(String className) {
        return "L" + TypeUtils.getInternalName(className) + ";";
    }

    public static String getGetterMethodName(String fieldName) {
        return "get" + StringUtils.fistCharToUpperCase(fieldName);
    }

    public static String getSetterMethodName(String fieldName) {
        return "set" + StringUtils.fistCharToUpperCase(fieldName);
    }

}
