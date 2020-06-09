package com.wujiuye.jcg.handler;

import com.wujiuye.jcg.tree.AnnotationNode;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * 字段注解赋值
 *
 * @author wujiuye 2020/06/09
 */
public class ClassAnnotionHandler extends AnnotationHandler {

    private ClassWriter classWriter;

    public ClassAnnotionHandler(ClassWriter classWriter, List<AnnotationNode> annotations) {
        super(annotations);
        this.classWriter = classWriter;
    }

    @Override
    protected AnnotationVisitor getVisitor(AnnotationNode annotation) {
        return classWriter.visitAnnotation(Type.getDescriptor(annotation.getAnnotationClass()), true);
    }

}
