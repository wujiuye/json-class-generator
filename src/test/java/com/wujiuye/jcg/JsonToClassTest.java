package com.wujiuye.jcg;

import com.google.gson.Gson;
import org.junit.Test;

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
        Class<?> cls = JcgClassFactory.getInstance().generateResponseClassByJson("com.wujiuye.jcg.model.OfferBean", json);
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
