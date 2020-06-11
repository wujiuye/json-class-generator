# JsonClassGenerator
[![Maven central](https://user-gold-cdn.xitu.io/2020/6/11/172a3531ced621f6?w=182&h=20&f=svg&s=1038)](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/json-class-generator)

`JCG`(`json-class-generator`)是一个可用于运行时根据`json`生成`class`的工具，可能使用场景不多。由于是运行时生成的`class`，所以生成的`class`也只能通过反射去使用。

## 技术栈
* `gson`：`json`解析工具；
* `asm`：字节码生成工具；

## 特性
* 将`json`解析生成`class`，为`class`添加字段和对应字段的`get`、`set`方法；
* 支持为生成的`class`添加注解，使用注解规则声明将注解添加在类或是字段上；

## 基础功能：将json解析生成class

为验证结果的正确性，可配置将本工具包生成的`class`输出到文件，通过`idea`打开可以查看生成的`java`代码。

```java
public class JsonToClassTest {

    static {
        // value为输出的目录
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }
}
```

既然要用到动态`json`解析生成`class`，那么说明`json`我们是通过`API`或者读取数据库获取的。为了简单，我们就直接定义`json`字符串来测试了。

假设`json`为：
```json
{
  "name":"name",
  "price":1.0,
  "nodes":[
     {
      "id":222,
      "note":"xxx"
     }
  ]
}
```

那么我们期望`JCG`工具为我们生成的`class`应该是这样的：
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

现在我们开始使用`JCG`工具将上面例子中的`json`解析生成`class`
```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test() {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.TestBean";
        
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

## 特色：为生成的class添加注解

为了使用某些框架的特性，我们可能需要在将`json`解析生成`class`时，就需要为`class`添加注解。比如我们想将`JCG`生成的`class`用于后续的`json`解析，那么我们可能需要在`class`上或者字段上添加一些诸如`@JsonIgnore`之类的注解。这些需求`JCG`都可以满足，目前的不足之处是还不支持注解的属性是数组类型。

假设，我们想在解析`json`生成的`class`上添加一个`@TestAnnno`注解，可以这么实现：
```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test() {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.TestBean";
        
        // 注解规则
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

注解规则映射类`AnnotationRule`的构造方法说明：
* 参数`1`：要添加的注解的类型；
* 参数`2`：注解在类上还是字段上，第二个参数和第三个参数需要配和使用。（只支持添加在类上、添加在字段上两种类型）；
* 参数`3`：添加的路径，`JCG`根据路径通过深度遍历寻找目标字段，如果是添加在类上，则会加在目录字段对应的`class`上；

如果参数`2`为`ElementType.TYPE`，参数`3`为`""`，那么结果就是在`json`生成的`class`上添加注解；如果参数`2`为`ElementType.FIELD`，参数`3`为`""`则会报错，但如果参数`3`为`"name"`，则是在`name`字段上添加注解。

在前面给出的`json`例子中，由于`nodes`字段是一个数组，且数组元素不是基本数据类型，因此`JCG`也会为数组元素生成一个`class`。如果想为`nodes`元素类型对应的`class`也添加注解，那么可以通过`path`实现。例如：

* 参数`2`为`ElementType.TYPE`，参数`3`为`"nodes"`，则会在`nodes`元素对应的类上添加注解；
* 参数`2`为`ElementType.FIELD`，参数`3`为`"nodes"`，则只是在`nodes`字段上添加注解；
* 参数`2`为`ElementType.FIELD`，参数`3`为`"nodes.id"`，则会在`nodes`元素对应的类的`id`字段上添加注解；

代码如下
```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test()  {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.TestBean";
        
        // 给nodes数组的元素类型class添加@TestAnno
        AnnotationRule fieldRule = new AnnotationRule(TestAnno.class, ElementType.TYPE, "nodes");
        fieldRule.putAttr("value", "12233");
        AnnotationRuleRegister.registRule(className, fieldRule);
        
        // 给nodes数组元素类型class的id字段添加注解@TestAnno
        AnnotationRule fieldClassRule = new AnnotationRule(TestAnno.class, ElementType.FIELD, "nodes.id");
        fieldClassRule.putAttr("type", ElementType.FIELD);
        AnnotationRuleRegister.registRule(className, fieldClassRule);
        
        // 调用JcgClassFactory的generateResponseClassByJson方法即可生成Class
        Class<?> cls = JcgClassFactory.getInstance()
                    .generateResponseClassByJson(className, json);
    }

}
```

注解规则映射类`AnnotationRule`的`putAttr`方法说明：
* `key`: 对应注解的属性名；
* `value`: 对应注解的属性值，属性值必须是基本数据类型、`String`、枚举、注解，目前不支持数组；

如果注解的属性也是注解类型，那么可以通过`putAttr`方法给`AnnotationRule`添加一个`AnnotationRule`实现，代理如下：

```java
public class JsonToClassTest {

    static {
        System.setProperty("jcg.classSavaPath", "/Users/wjy/MyProjects/JsonClassGenerator");
    }

    @Test
    public void test() {
        String json = "{\"name\":\"offer name\",\"price\":1.0,\"nodes\":[{\"id\":222,\"note\":\"xxx\"}]}";
        String className = "com.wujiuye.jcg.model.TestBean";

        AnnotationRule fieldRule = new AnnotationRule(TestAnno.class, ElementType.TYPE, "nodes");
        fieldRule.putAttr("value", "12233");
        // 设置@TestAnno的map属性，该属性类型为注解类型
        AnnotationRule annoRule = new AnnotationRule(Map.class, null, "");
        annoRule.putAttr("value", "haha");
        // 
        fieldRule.putAttr("map", annoRule);
        AnnotationRuleRegister.registRule(className, fieldRule);

        // 调用JcgClassFactory的generateResponseClassByJson方法即可生成Class
        Class<?> cls = JcgClassFactory.getInstance()
                    .generateResponseClassByJson(className, json);
    }

}
```

生成的结果如下：
```java
@TestAnno(
    value = "12233",
    map = @Map("haha")
)
public class TestBean$$Nodes {
    private Integer id;
    private String note;
    // get/set省略
}
```

`TestBean$$Nodes`是`JCG`为`json`中的`nodes`字段生成的一个`class`。
