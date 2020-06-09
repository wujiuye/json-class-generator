package com.wujiuye.jcg;

import com.wujiuye.jcg.tree.FieldNode;
import com.wujiuye.jcg.util.TypeUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.F_FULL;

/**
 * 为字段生成get、set方法
 *
 * @author wujiuye 2020/06/09
 */
public class FieldGetSetHandler implements MethodHandler {

    private String className;
    private ClassWriter classWriter;
    private List<FieldNode> fields;

    public FieldGetSetHandler(ClassWriter classWriter, String className) {
        this.classWriter = classWriter;
        this.className = className;
    }

    public void setFields(List<FieldNode> fields) {
        this.fields = fields;
    }

    @Override
    public void generateMethod() {
        for (FieldNode fieldNode : this.fields) {
            this.visitGetter(fieldNode);
            this.visitSetter(fieldNode);
        }
    }

    private void visitGetter(FieldNode fieldNode) {
        MethodVisitor getMv;
        if (!fieldNode.isArray()) {
            getMv = classWriter.visitMethod(ACC_PUBLIC,
                    TypeUtils.getGetterMethodName(fieldNode.getFieldName()),
                    Type.getMethodDescriptor(Type.getType(fieldNode.getCls())),
                    null, null);
        } else {
            String methodSig = "()Ljava/util/List<" + Type.getDescriptor(fieldNode.getCls()) + ">;";
            getMv = classWriter.visitMethod(ACC_PUBLIC,
                    TypeUtils.getGetterMethodName(fieldNode.getFieldName()),
                    Type.getMethodDescriptor(Type.getType(List.class)),
                    methodSig, null);
        }

        getMv.visitCode();
        getMv.visitVarInsn(ALOAD, 0);
        getMv.visitFieldInsn(GETFIELD,
                TypeUtils.getInternalName(className),
                fieldNode.getFieldName(),
                fieldNode.isArray() ? Type.getDescriptor(List.class) : Type.getDescriptor(fieldNode.getCls()));

        generateReturn(getMv, fieldNode.getCls());
        getMv.visitFrame(F_FULL, 0, null, 0, null);
        getMv.visitMaxs(2, 2);
        getMv.visitEnd();
    }

    private void visitSetter(FieldNode fieldNode) {
        MethodVisitor setMv;
        if (!fieldNode.isArray()) {
            setMv = classWriter.visitMethod(ACC_PUBLIC,
                    TypeUtils.getSetterMethodName(fieldNode.getFieldName()),
                    Type.getMethodDescriptor(Type.getType(void.class), Type.getType(fieldNode.getCls())),
                    null, null);
        } else {
            String methodSig = "(Ljava/util/List<" + Type.getDescriptor(fieldNode.getCls()) + ">;)V";
            setMv = classWriter.visitMethod(ACC_PUBLIC,
                    TypeUtils.getSetterMethodName(fieldNode.getFieldName()),
                    Type.getMethodDescriptor(Type.getType(void.class), Type.getType(List.class)),
                    methodSig, null);
        }

        setMv.visitCode();
        setMv.visitVarInsn(ALOAD, 0);
        setMv.visitVarInsn(ALOAD, 1);
        setMv.visitFieldInsn(PUTFIELD,
                TypeUtils.getInternalName(className),
                fieldNode.getFieldName(),
                fieldNode.isArray() ? Type.getDescriptor(List.class) : Type.getDescriptor(fieldNode.getCls()));
        generateReturn(setMv, void.class);

        setMv.visitFrame(F_FULL, 0, null, 0, null);
        setMv.visitMaxs(2, 2);
        setMv.visitEnd();
    }

}
