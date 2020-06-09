package com.wujiuye.jcg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注解规则注册器
 *
 * @author wujiuye 2020/06/09
 */
public class AnnotationRuleRegister {

    private static final AnnotationRuleRegister register = new AnnotationRuleRegister();

    private static final ThreadLocal<Map<String, List<AnnotationRule>>> threadLocal = new ThreadLocal<>();

    private static void check() {
        if (threadLocal.get() == null) {
            threadLocal.set(new HashMap<>());
        }
    }

    public static void registRule(String className, AnnotationRule annotationRule) {
        check();
        List<AnnotationRule> rules = threadLocal.get().computeIfAbsent(className, k -> new ArrayList<>());
        rules.add(annotationRule);
    }

    public static List<AnnotationRule> getRules(String className) {
        check();
        return threadLocal.get().get(className);
    }

    public static void remove() {
        threadLocal.remove();
    }

}
