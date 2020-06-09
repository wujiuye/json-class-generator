package com.wujiuye.jcg.handler;

import com.wujiuye.jcg.tree.AnnotationNode;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * 字段注解赋值
 *
 * @author wujiuye 2020/06/09
 */
public class FieldAnnotionHandler extends AnnotationHandler {

    private FieldVisitor fieldVisitor;

    public FieldAnnotionHandler(FieldVisitor fieldVisitor, List<AnnotationNode> annotations) {
        super(annotations);
        this.fieldVisitor = fieldVisitor;
    }

    @Override
    protected AnnotationVisitor getVisitor(AnnotationNode annotation) {
        return fieldVisitor.visitAnnotation(Type.getDescriptor(annotation.getAnnotationClass()), true);
    }

}
