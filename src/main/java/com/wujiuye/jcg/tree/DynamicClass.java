package com.wujiuye.jcg.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 类节点
 *
 * @author wujiuye 2020/06/08
 */
public class DynamicClass {

    private String className;
    private List<FieldNode> fields;

    public DynamicClass(String className) {
        this.className = className;
        this.fields = new ArrayList<>();
    }

    public void addField(FieldNode fieldNode) {
        this.fields.add(fieldNode);
    }

    public String getClassName() {
        return className;
    }

    public List<FieldNode> getFields() {
        return fields;
    }

}
