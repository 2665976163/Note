# Spring

我们将bean交给spring到底spring底层做了什么事情？怎么存储我们的bean和怎样初始化bean的？

**一、bean的生命周期**

```mysql
用户将bean交给spring容器 --> 容器将bean封装到BeanDefinition的子类中 -->
将BeanDefinition读取到map中 --> 由处理器进行初始化操作 --> 对象最终存入spring单例池中 
#主要核心就这么几个操作
```

什么是BeanDefinition？

```java
用户将bean交给spring的时候带了一些手动配置跟默认配置，比如说是否是单例，是否有自动装配，类名是什么，类型是什么等，若用户提供多个不同的bean带着不同的配置，那么spring该怎么将这些bean进行统一管理？就是将spring需要的东西抽取出一个类，然后spring将用户提供的bean封装到这个类中，这样spring就可以进行统一管理，这个bean就是beanDefinition.
```



**二、提供自定义处理器**

​		说通常点spring可以说是一个超级bean工厂，主要用于生产、存储、管理bean，平常我们将想将某个bean交给spring的时候只需要在该类上声明@Component || Java类配置 又或者说是xml配置等一些其他可以将类交给spring容器的操作，就不用spring管内部到底做了什么操作，我们只清楚当需要某个bean时依靠注解Autowired或Resource等其他注解就可以将我们存入spring容器的bean拿出来供我们使用，但仅仅是这样的话有没有想过一个问题，就是说spring帮我们管理bean或者说spring帮我们管理了关于bean的很多东西，我们可不可以和spring一起管理这些东西？这个当然是可以的，整个bean容器主要是由spring提供的一系列处理器构建出来的，开发者可以向spring提供自己的处理器帮助spring构建spring容器

**案例演示**

**【条件】**实现BeanFactoryPostProcessor并交给spring容器.

**【内容】**Test1类与Test2类，将Test1交给spring拿出Test2

```java
@Component
public class Test1{}
//Test2 没有交给spring
public class Test2{}
```

**自定义处理器**

```java
@Component
public class MyProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory BeanFactory){
        //取出用户提供给spring容器的类将该类BeanDefinition向下转型获取更多方法
        GenericBeanDefinition test1 = 
            (GenericBeanDefinition) BeanFactory.getBeanDefinition("test1");
        //将test1原本描述是Test.class的类型换成Test2
        //当用户取Test1的时候就变成了Test2，而Test2是没有被spring扫描到的.
        //这样我们就完成了与spring一起管理的第一步.
        test1.setBeanClass(Test2.class);
    }
}
```

