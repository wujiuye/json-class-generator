package com.wujiuye.jcg;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
public @interface TestAnno {

    String value();

    ElementType type();

    Map map();

    Map[] mapArray();

    ElementType[] typeArray();

}
