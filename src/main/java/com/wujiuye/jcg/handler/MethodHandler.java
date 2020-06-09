package com.wujiuye.jcg.handler;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * 方法处理器
 *
 * @author wujiuye 2020/06/09
 */
public interface MethodHandler {

    /**
     * 生成方法
     */
    void generateMethod();

    /**
     * 生成返回方法
     *
     * @param mv
     * @param returnClas
     */
    default void generateReturn(MethodVisitor mv, Class<?> returnClas) {
        if (returnClas == void.class) {
            mv.visitInsn(RETURN);
        } else if (returnClas == int.class) {
            mv.visitInsn(IRETURN);
        } else if (returnClas == long.class) {
            mv.visitInsn(LRETURN);
        } else if (returnClas == float.class) {
            mv.visitInsn(FRETURN);
        } else if (returnClas == double.class) {
            mv.visitInsn(DRETURN);
        } else {
            mv.visitInsn(ARETURN);
        }
    }

}
