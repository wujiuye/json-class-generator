package com.wujiuye.jcg;

import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解规则
 *
 * @author wujiuye 2020/06/09
 */
public class AnnotationRule {

    private ElementType elementType;
    /**
     * 支持级联设置:如：offer.name，则注解在字段上，如果是offer，且类型为ElementType.Type，则注解在类上
     * 如果path为NUll，则注解在根类上
     */
    private String path;
    private Class<?> annoClas;
    private Map<String, Object> annoAttrs;

    public AnnotationRule(Class<?> annoClas, ElementType type, String path) {
        this.annoClas = annoClas;
        this.elementType = type;
        this.path = path;
        this.annoAttrs = new HashMap<>();
    }

    public AnnotationRule putAttr(String key, Object value) {
        this.annoAttrs.putIfAbsent(key, value);
        return this;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public String getPath() {
        return path;
    }

    public Class<?> getAnnoClas() {
        return annoClas;
    }

    public Map<String, Object> getAnnoAttrs() {
        return annoAttrs;
    }

}
