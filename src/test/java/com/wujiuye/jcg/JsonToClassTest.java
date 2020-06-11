package com.wujiuye.jcg;

import com.google.gson.Gson;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;

/**
 * @author wujiuye 2020/06/08
 */
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test() throws ClassNotFoundException {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.OfferBean";

        AnnotationRule annotationRule = new AnnotationRule(TestAnno.class, ElementType.TYPE, "");
        annotationRule.putAttr("value", "122");
        AnnotationRuleRegister.registRule(className, annotationRule);

        AnnotationRule fieldClassRule = new AnnotationRule(TestAnno.class, ElementType.FIELD, "nodes.id");
        fieldClassRule.putAttr("type", ElementType.FIELD);
        AnnotationRuleRegister.registRule(className, fieldClassRule);

        AnnotationRule fieldRule = new AnnotationRule(TestAnno.class, ElementType.TYPE, "nodes");
        fieldRule.putAttr("value", "12233");
        // 注解的属性类型为注解的写法
        AnnotationRule annoRule = new AnnotationRule(Map.class, null, "");
        annoRule.putAttr("value", "haha");

        fieldRule.putAttr("map", annoRule);
        AnnotationRuleRegister.registRule(className, fieldRule);

        Class<?> cls = JcgClassFactory.getInstance().generateResponseClassByJson(className, json);
        try {
            Object obj = new Gson().fromJson(json, cls);
            System.out.println(obj.getClass());
            Method method = obj.getClass().getDeclaredMethod("getNodes");
            Object result = method.invoke(obj);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
