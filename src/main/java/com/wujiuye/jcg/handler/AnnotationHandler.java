package com.wujiuye.jcg.handler;

import com.wujiuye.jcg.tree.AnnotationNode;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

/**
 * @author wujiuye 2020/06/09
 */
public abstract class AnnotationHandler {

    private List<AnnotationNode> annotations;

    public AnnotationHandler(List<AnnotationNode> annotations) {
        this.annotations = annotations;
    }

    protected abstract AnnotationVisitor getVisitor(AnnotationNode annotation);

    public void generateAnnotation() {
        if (annotations == null || annotations.isEmpty()) {
            return;
        }
        annotations.forEach(annotationNode -> {
            AnnotationVisitor visitor = getVisitor(annotationNode);
            Map<String, Object> attrs = annotationNode.getKeyValue();
            this.generateAnnotation(visitor, attrs);
        });
    }

    private void generateAnnotation(AnnotationVisitor visitor, Map<String, Object> attrs) {
        if (attrs == null || attrs.size() == 0) {
            visitor.visitEnd();
            return;
        }
        attrs.entrySet().forEach(attr -> {
            // 对AnnotationNode递归解析
            if (attr.getValue() instanceof AnnotationNode) {
                AnnotationNode node = (AnnotationNode) attr.getValue();
                AnnotationVisitor attVisitor = visitor.visitAnnotation(attr.getKey(), Type.getDescriptor(node.getAnnotationClass()));
                Map<String, Object> attAttrs = node.getKeyValue();
                this.generateAnnotation(attVisitor, attAttrs);
                return;
            }
            Class<?> cls = attr.getValue().getClass();
            if (cls.isEnum()) {
                visitor.visitEnum(attr.getKey(), Type.getDescriptor(cls), attr.getValue().toString());
            } else if (attr.getValue().getClass().isArray()) {
                AnnotationVisitor arrayVisitor = visitor.visitArray(attr.getKey());
                Object[] objects = (Object[]) attr.getValue();
                for (Object obj : objects) {
                    // 数组元素类型是注解
                    if (obj instanceof AnnotationNode) {
                        AnnotationNode node = (AnnotationNode) obj;
                        AnnotationVisitor attVisitor = arrayVisitor.visitAnnotation(attr.getKey(), Type.getDescriptor(node.getAnnotationClass()));
                        Map<String, Object> attAttrs = node.getKeyValue();
                        this.generateAnnotation(attVisitor, attAttrs);
                    } else if (obj.getClass().isEnum()) {
                        arrayVisitor.visitEnum(null, Type.getDescriptor(obj.getClass()), obj.toString());
                    } else {
                        arrayVisitor.visit(null, obj);
                    }
                }
                arrayVisitor.visitEnd();
            } else {
                visitor.visit(attr.getKey(), attr.getValue());
            }
        });
        visitor.visitEnd();
    }

}
