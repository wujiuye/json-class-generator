package com.wujiuye.jcg.tree;

import com.google.gson.*;
import com.wujiuye.jcg.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 将json解析为类定义元数据
 *
 * @author wujiuye 2020/06/08
 */
public class JsonClassDefinitionUtils {

    /**
     * 是否忽略空字段
     */
    private static boolean IGNORE_NULL_FIELD;

    static {
        IGNORE_NULL_FIELD = Boolean.getBoolean("jcg.ignore_null_field");
    }

    private final static String INNER_CLASS_NAME_SPL = "$$";

    public static DynamicClass analysis(String className, String json) {
        DynamicClass dynamicClass = new DynamicClass(className);
        JsonElement element = new JsonParser().parse(json);
        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            if (array.size() == 0) {
                throw new RuntimeException("json解析失败，数组至少要有一个元素！");
            }
            element = array.get(0);
        }
        if (!element.isJsonObject()) {
            throw new RuntimeException("json解析失败!");
        }
        JsonObject jsonObj = element.getAsJsonObject();
        jsonObj.entrySet().forEach(el -> analysisElement(className, el.getKey(), el.getValue())
                .ifPresent(dynamicClass::addField));
        return dynamicClass;
    }

    private static Optional<FieldNode> analysisElement(String className, String name, JsonElement value) {
        FieldNode fieldNode = null;
        if (value.isJsonPrimitive()) {
            JsonPrimitive primitive = value.getAsJsonPrimitive();
            fieldNode = analysisElement(name, primitive);
        } else if (value.isJsonObject()) {
            String innerClass = className + INNER_CLASS_NAME_SPL + StringUtils.fistCharToUpperCase(name);
            fieldNode = new FieldNode(name, innerClass, analysis(innerClass, value.toString()));
        } else if (value.isJsonArray()) {
            JsonArray array = value.getAsJsonArray();
            if (array.size() == 0) {
                if (!IGNORE_NULL_FIELD) {
                    throw new RuntimeException("数组为空，解析不了！");
                }
                return Optional.empty();
            }
            JsonElement one = array.get(0);
            if (one.isJsonPrimitive()) {
                fieldNode = analysisElement(name, one.getAsJsonPrimitive());
            } else if (one.isJsonObject()) {
                String innerClass = className + INNER_CLASS_NAME_SPL + StringUtils.fistCharToUpperCase(name);
                fieldNode = new FieldNode(name, innerClass, analysis(innerClass, one.toString()));
            } else {
                throw new RuntimeException("暂不支持数组嵌套！");
            }
            fieldNode.setArray(true);
        } else if (value.isJsonNull()) {
            if (!IGNORE_NULL_FIELD) {
                throw new RuntimeException("不要在json中写null值，无法解析null的类型！如果运行忽略null字段，可设置jcg.ignore_null_field=true!");
            }
        }
        return Optional.ofNullable(fieldNode);
    }

    private static FieldNode analysisElement(String name, JsonPrimitive value) {
        FieldNode fieldNode = null;
        if (value.isNumber()) {
            String strValue = value.getAsString();
            // 含有小数点才生成BigDecimal类型
            if (strValue.contains(".")) {
                fieldNode = new FieldNode(name, BigDecimal.class);
            } else {
                // 小于10位数使用Integer
                if (strValue.length() < 10) {
                    fieldNode = new FieldNode(name, Integer.class);
                } else {
                    fieldNode = new FieldNode(name, Long.class);
                }
            }
        } else if (value.isString()) {
            fieldNode = new FieldNode(name, String.class);
        } else if (value.isBoolean()) {
            fieldNode = new FieldNode(name, Boolean.class);
        }
        return fieldNode;
    }

}
