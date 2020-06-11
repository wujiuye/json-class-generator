# json-class-generator
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/json-class-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/json-class-generator)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

用于`运行时`根据`json`生成`Class`的工具，可以使用场景不多。
由于是运行时生成的class，所以生成的class也只能通过反射去使用。

## 技术栈
* gson：json解析工具；
* asm：字节码生成工具；

## 特性
* 将json解析生成class，为class添加字段和对应字段的getter、setter；
* 支持为生成的class添加注解，使用注解规则声明将注解添加在类或是字段上；

## 将json解析生成class

为验证结果的正确性，可配置将本工具包生成的class输出到文件，通过idea打开可以查看生成的java代码。
```java
public class JsonToClassTest {

    static {
        // value为输出的目录
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }
}
```

既然要用到动态json解析生成class，那么说明json我们是通过API或者读取数据库获取的。为了简单，我们就直接
定义json字符串来测试了。

假设json为：
```json
{
  "name":"offer name",
  "price":1.0,
  "nodes":[
     {
      "id":222,
      "note":"xxx"
     }
  ]
}
```

那么我们期望JCG工具为我们生成的class应该是这样的：
```java
public class Xxx {
    private String name;
    private BigDecimal price;
    private List<Yyy> nodes;
    // get、set方法省略
}
public class Yyy {
    private Integer id;
    private String note;
    // get、set方法省略
}
```

现在我们开始使用JCG工具将上面例子中的json解析生成class
```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test() {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.OfferBean";
        // 我们需要为这串json定义一个类型名，
        // 然后调用JcgClassFactory的generateResponseClassByJson方法即可生成Class
        Class<?> cls = JcgClassFactory.getInstance()
                                .generateResponseClassByJson(className, json);
        try {
            // 验证生成的class是否正确
            Object obj = new Gson().fromJson(json, cls);
            System.out.println(obj.getClass());
            // 验证生成的get/set方法是否能正常调用
            Method method = obj.getClass().getDeclaredMethod("getNodes");
            Object result = method.invoke(obj);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```

结果省略。

## 为生成的class添加注解

为了使用某些框架的特性，我们可能需要在将json解析生成class时，就需要为class添加注解。比如我们想
将JCG生成的class用于后续的json解析，那么我们可能需要在class上或者字段上添加一些诸如`@JsonIgnore`
之类的注解。这些需求JCG都可以满足大家，不过目前还不太完善，例如目前还不支持注解的属性是数组类型。

假设，我们想在json生成的class上添加一个@TestAnnno注解，可以这么实现：
```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test() {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.OfferBean";
        
        AnnotationRule annotationRule = new AnnotationRule(TestAnno.class, ElementType.TYPE, "");
        annotationRule.putAttr("value", "122");
        // 注册注解规则
        AnnotationRuleRegister.registRule(className, annotationRule);

        // 调用JcgClassFactory的generateResponseClassByJson方法即可生成Class
        Class<?> cls = JcgClassFactory.getInstance()
                    .generateResponseClassByJson(className, json);
    }

}
```

注解规则映射类AnnotationRule的构造方法说明：
* 参数1：要添加的注解的类型；
* 参数2：注解在类上还是字段上，第二个参数和第三个参数需要配和使用。（目前只支持类、字段两个类型）；
* 参数3：添加的路径；

如果参数2为ElementType.TYPE，参数3为""，那么结果就是在json生成的class上添加注解；
如果参数2为ElementType.FIELD，参数3为""则会报错，如果参数3为"name"，则是在name字段上添加注解；

由于nodes字段是一个数组，如果数组元素不是基本数据类型，那么JCG也会为数组元素生成一个class，如果想为nodes
元素类型对应的class也添加注解，那么可以通过`path`指定。如：

* 参数2为ElementType.TYPE，参数3为"nodes"，则会在nodes元素对应的类上添加注解；
* 参数2为ElementType.FIELD，参数3为"nodes"，则只是在nodes字段上添加注解；
* 参数2为ElementType.FIELD，参数3为"nodes.id"，则会在nodes元素对应的类的id字段上添加注解；

```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test()  {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.OfferBean";

        AnnotationRule fieldRule = new AnnotationRule(TestAnno.class, ElementType.TYPE, "nodes");
        fieldRule.putAttr("value", "12233");
        AnnotationRuleRegister.registRule(className, fieldRule);

        AnnotationRule fieldClassRule = new AnnotationRule(TestAnno.class, ElementType.FIELD, "nodes.id");
        fieldClassRule.putAttr("type", ElementType.FIELD);
        AnnotationRuleRegister.registRule(className, fieldClassRule);
        
        // 调用JcgClassFactory的generateResponseClassByJson方法即可生成Class
        Class<?> cls = JcgClassFactory.getInstance()
                    .generateResponseClassByJson(className, json);
    }

}
```

注解规则映射类AnnotationRule的putAttr方法说明：
* key: 对应注解的属性名；
* value: 对应注解的属性值，属性值必须是基本数据类型、String、枚举、注解，目前不支持数组；


如果注解的属性也是注解类型，那么可以通过putAttr方法给AnnotationRule添加一个AnnotationRule实现，
例如：
```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test() {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.OfferBean";

        AnnotationRule fieldRule = new AnnotationRule(TestAnno.class, ElementType.TYPE, "nodes");
        fieldRule.putAttr("value", "12233");
        // 注解的属性类型为注解的写法
        AnnotationRule annoRule = new AnnotationRule(Map.class, null, "");
        annoRule.putAttr("value", "haha");
        fieldRule.putAttr("map", annoRule);
        AnnotationRuleRegister.registRule(className, fieldRule);

        // 调用JcgClassFactory的generateResponseClassByJson方法即可生成Class
        Class<?> cls = JcgClassFactory.getInstance()
                    .generateResponseClassByJson(className, json);
    }

}
```

结果如下：
```java
@TestAnno(
    value = "12233",
    map = @Map("haha")
)
public class OfferBean$$Nodes {
    private Integer id;
    private String note;
}
```




