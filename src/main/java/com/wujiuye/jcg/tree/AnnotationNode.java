package com.wujiuye.jcg.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * 注解节点
 *
 * @author wujiuye 2020/06/08
 */
public class AnnotationNode {

    private static final Class<?>[] PRIMITIVE_TYPES = {int.class, long.class, short.class,
            float.class, double.class, byte.class, boolean.class, char.class, String.class};

    private Class<?> annotationClass;
    private Map<String, Object> keyValue;

    public AnnotationNode(Class<?> annotationClass) {
        this.annotationClass = annotationClass;
        this.keyValue = new HashMap<>();
    }

    public Class<?> getAnnotationClass() {
        return annotationClass;
    }

    public Map<String, Object> getKeyValue() {
        return keyValue;
    }

    public AnnotationNode putAttr(String key, Object value) {
        Class<?> vclas = value.getClass();
        for (Class<?> suppor : PRIMITIVE_TYPES) {
            if (suppor == vclas) {
                this.keyValue.put(key, value);
                return this;
            }
        }
        throw new RuntimeException("不支持的类型，注解只支持常量！");
    }

}
