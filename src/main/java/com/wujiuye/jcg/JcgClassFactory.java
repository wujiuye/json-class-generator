package com.wujiuye.jcg;

import com.wujiuye.jcg.tree.JsonClassDefinitionUtils;
import com.wujiuye.jcg.tree.DynamicClass;
import com.wujiuye.jcg.util.JcgClassLoader;

/**
 * Response生成器
 *
 * @author wujiuye 2020/06/08
 */
public class JcgClassFactory {

    private static final JcgClassFactory factory = new JcgClassFactory();
    private JcgClassLoader codeClassLoader;

    private JcgClassFactory() {
        this.codeClassLoader = new JcgClassLoader(Thread.currentThread().getContextClassLoader());
    }

    public static JcgClassFactory getInstance() {
        return factory;
    }

    public Class<?> generateResponseClassByJson(String className, String json) throws ClassNotFoundException {
        try {
            return codeClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            DynamicClass dynamicClass = JsonClassDefinitionUtils.analysis(className, json);
            JsonToClassHandler responseHandler = new JsonToClassHandler(dynamicClass);
            codeClassLoader.add(className, responseHandler);
            return codeClassLoader.loadClass(className);
        }
    }

    public JcgClassLoader getCodeClassLoader() {
        return codeClassLoader;
    }

}
