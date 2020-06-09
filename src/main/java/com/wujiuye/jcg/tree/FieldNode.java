package com.wujiuye.jcg.tree;

/**
 * 字段节点
 *
 * @author wujiuye 2020/06/08
 */
public class FieldNode {

    private String fieldName;
    private Class<?> cls;
    private String claName;
    private DynamicClass dynamicClass;
    /**
     * 如果是数组，则rClass表示数组的元素类型
     */
    private boolean array = false;

    public FieldNode(String fieldName, Class<?> cls) {
        this.fieldName = fieldName;
        this.cls = cls;
    }

    public FieldNode(String fieldName, String fieldClassName, DynamicClass dynamicClass) {
        this.fieldName = fieldName;
        this.claName = fieldClassName;
        this.dynamicClass = dynamicClass;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getClaName() {
        return claName;
    }

    public DynamicClass getDynamicClass() {
        return dynamicClass;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

}
