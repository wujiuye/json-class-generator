package com.wujiuye.jcg.util;

import com.wujiuye.jcg.ClassHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义类加载器
 *
 * @author wujiuye
 * @version 1.0 on 2019/11/25
 */
public class JcgClassLoader extends ClassLoader {

    /**
     * 缓存ByteCodeHandler，不重复生成
     * class由jvm调用ClassLoader缓存了，不需要自己缓存
     */
    private final Map<String, ClassHandler> classes = new HashMap<>();

    public JcgClassLoader(final ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        ClassHandler handler = classes.get(name);
        if (handler != null) {
            byte[] bytes = handler.getByteCode();
            ClassFileUtils.savaToClasspath(handler.getClassName(), bytes);
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }

    public void add(final String name, final ClassHandler handler) {
        classes.put(name, handler);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

}
