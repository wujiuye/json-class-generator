package com.wujiuye.jcg.handler;

import com.wujiuye.jcg.JcgClassFactory;
import com.wujiuye.jcg.tree.FieldNode;
import com.wujiuye.jcg.tree.DynamicClass;
import com.wujiuye.jcg.util.TypeUtils;
import org.objectweb.asm.*;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * 根据json生成Class
 *
 * @author wujiuye 2020/06/08
 */
public class JsonToClassHandler implements ClassHandler {

    private ClassWriter classWriter;
    private DynamicClass dynamicClass;

    public JsonToClassHandler(DynamicClass dynamicClass) {
        this.dynamicClass = dynamicClass;
    }

    @Override
    public String getClassName() {
        return this.dynamicClass.getClassName();
    }

    private void generateClassInfo() {
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        // String signature = TypeUtils.getSignature(dynamicClass.getClassName());
        this.classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC,
                TypeUtils.getInternalName(dynamicClass.getClassName()),
                // signature, 不是泛型不要指定签名，否则会导致调用class.getGenericSuperclass()获取到的永远是自己
                // 导致json解析框架解析异常，fastjson报StackOverflowError，gson报：字段重复定义
                null,
                Type.getInternalName(Object.class), null);
        AnnotationHandler annotationHandler = new ClassAnnotionHandler(classWriter, dynamicClass.getAnnotations());
        annotationHandler.generateAnnotation();
    }

    private void generateInitMethod() {
        MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "<init>",
                "()V",
                null, null);
        mv.visitCode();

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL,
                Type.getInternalName(Object.class),
                "<init>", "()V", false);

        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void ensureFieldNodeClsExist(FieldNode fieldNode) {
        if (fieldNode.getCls() == null) {
            if (fieldNode.getClaName() == null || fieldNode.getDynamicClass() == null) {
                throw new NullPointerException("field class name is null or field rclass is null");
            }
            JsonToClassHandler responseHandler = new JsonToClassHandler(fieldNode.getDynamicClass());
            JcgClassFactory.getInstance().getCodeClassLoader().add(fieldNode.getClaName(), responseHandler);
            try {
                fieldNode.setCls(JcgClassFactory.getInstance().getCodeClassLoader().loadClass(fieldNode.getClaName()));
            } catch (ClassNotFoundException e) {
                throw new NullPointerException("generateFields error by inner class not found " + fieldNode.getClaName());
            }
        }
    }

    private void generateFields() {
        for (FieldNode fieldNode : dynamicClass.getFields()) {
            this.ensureFieldNodeClsExist(fieldNode);
            FieldVisitor fieldVisitor;
            if (!fieldNode.isArray()) {
                String fieldSig = Type.getDescriptor(fieldNode.getCls());
                fieldVisitor = classWriter.visitField(ACC_PRIVATE, fieldNode.getFieldName(),
                        Type.getDescriptor(fieldNode.getCls()), fieldSig, null);
            } else {
                String fieldSig = "Ljava/util/List<" + Type.getDescriptor(fieldNode.getCls()) + ">;";
                fieldVisitor = classWriter.visitField(ACC_PRIVATE, fieldNode.getFieldName(),
                        Type.getDescriptor(List.class),
                        fieldSig, null);
            }
            AnnotationHandler annotationHandler = new FieldAnnotionHandler(fieldVisitor, fieldNode.getAnnotations());
            annotationHandler.generateAnnotation();
        }
    }

    private void generateFieldGetterSetter() {
        FieldGetSetHandler fieldGetSetHandler = new FieldGetSetHandler(classWriter, dynamicClass.getClassName());
        fieldGetSetHandler.setFields(dynamicClass.getFields());
        fieldGetSetHandler.generateMethod();
    }

    @Override
    public byte[] getByteCode() {
        this.generateClassInfo();
        this.generateInitMethod();
        this.generateFields();
        this.generateFieldGetterSetter();
        this.classWriter.visitEnd();
        return this.classWriter.toByteArray();
    }

}
