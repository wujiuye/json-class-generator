package com.wujiuye.jcg.handler;

import com.wujiuye.jcg.tree.AnnotationNode;
import org.objectweb.asm.AnnotationVisitor;

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
            if (attrs == null || attrs.size() == 0) {
                return;
            }
            attrs.entrySet().forEach(attr -> visitor.visit(attr.getKey(), attr.getValue()));
        });
    }

}
