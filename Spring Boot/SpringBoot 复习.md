# SpringBoot

​	 Spring Boot使创建的项目是可独立运行的， 基于Spring的应用程序配置变得容易， 大多数Spring Boot应用程序只需要很少的Spring配置 



<u>**运行                                              **</u>

您可以使用Spring Boot将项目打成jar包，该jar包可以在cmd中使用`java -jar jar名称`启动

```cmd
java -jar jarName
```

 SpringBoot还提供了一个运行“spring脚本”的命令行工具

```tx
Spring Boot CLI
```



<u>**初衷                                              **</u>

**\>** 为所有Spring开发提供根本上更快且可广泛访问的入门经验。

**\>** 开箱即用，但随着需求开始偏离默认值，您会很快摆脱困境。

**\>** 提供一系列大型项目通用的非功能性功能（例如嵌入式服务器，安全性，指标，运行状况检查和外部化配置）。

**\>** 完全没有代码生成，也不需要XML配置









### 系统要求

 若使用Spring Boot 2.3.0 Java版本必须不能低于 Java8，且spring FrameWork 版本不能低于 5.2.3.

<u>**SpringBoot 构建支持                       **</u>

SpringBoot构建支持Maven  3.3+、Gradle 6.x（也支持5.6，但已弃用 )

<u>**SpringBoot 支持嵌入式servlet容器                              **</u>

SpringBoot支持嵌入式容器包含

| 名称         | Servlet 版本 |
| :----------- | :----------- |
| Tomcat 9.0   | 4.0          |
| Jetty 9.4    | 3.1          |
| Undertow 2.0 | 4.0          |

您还可以将Spring Boot应用程序部署到任何Servlet 3.1+兼容容器中。





### 入门程序

如果你有通过idea快速生成springboot项目，你可以去关注一下pom.xml  中这么一段代码

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.0.RELEASE</version>
</parent>
```

它表示当前pom文件将spring-boot-starter-parent.pom的东西继承下来，在spring-boot-starter-parent中提供了很多默认的配置，这些配置可以大大简化我们的开发 .

<u>**通过继承spring-boot-starter-parent，默认具备了如下功能                      **</u>

```java
Java版本（Java8）
源码的文件编码方式（UTF-8）
依赖管理
打包支持
明智的资源过滤
合理的插件配置
针对application.properties和application.yml包括特定于配置文件的文件（例如application-dev.properties和application-dev.yml）进行明智的资源过滤
```

 	请注意，由于`application.properties`和`application.yml`文件都接受Spring样式的占位符（`${…}`），因此Maven过滤已更改为使用`@..@`占位符。（您可以通过设置名为的Maven属性来覆盖它`resource.delimiter`。） 

​	以上继承来的特性有的并非直接继承自 <u>spring-boot-starter-parent</u>，而是继承自<u>spring-boot-starter-parent</u>的父级<u>spring-boot-dependencies

继承spring-boot-starter-parent后，大大简化了我们的配置，它提供了丰富的常用的默认的依赖的版本定义，我们就不需要再次指定版本号，例如：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

以上配置是也是构建springboot必须的配置，在构建Springboot入门项目时我们在配置文件中仅需要做俩步.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.znsd</groupId>
    <artifactId>SpringBoot</artifactId>
    <version>1.0-SNAPSHOT</version>
	<!-- 第一步 继承springboot提供的pom文件 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
    </parent>
    <!-- 第二步 导入Springboot所需要的依赖 -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

这样我们就完成了一个简单的SpringBoot的简单构建，但现在还仅仅是配置，接下来我们就可以开始入门了.



**<u>补充</u>**

因为springboot为我们提供了默认jar的版本号，假设我们需要定制自己的版本号，可以通过下面的方式重写，但并不建议这样做，因为可能个个jar包版本不统一导致项目出现问题.

```xml
<properties>
    <spring-data-releasetrain.version>Fowler-SR2</spring-data-releasetrain.version>
</properties>
```

若不想继承SpringBoot提供的pom文件也有方式可以创建springBoot项目

```xml
https://www.jianshu.com/p/628acadbe3d8
```



**<u>入门</u>**

上次我们已经做了springboot构建项目的基本配置，现在我们要完成第一个入门程序Hello SpringBoot！

首先在maven项目src/main/java下创建一个类[名称随意]

```java
package com.znsd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Test {
    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }
}
```

完成一个入门程序第一步是在类上加上一个注解，第二步在main方法中调用SpringApplication.run方法将当前类的.class与当前main的参数传入进去这样springBoot就可以正常运行了！

> @SpringBootApplication

```java
	在没有@SpringBootApplication出现之前要使用的注解有三个[声明后俩个也可以]声明springboot的入口@Configuration、@EnableAutoConfiguration、@ComponentScan，但每次声明三个过于麻烦，所以springboot就将这三个注解的功能归纳于一个注解中就是@SpringBootApplication.
```

> @Configuration

```java
	@Configuration用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法，
该配置类可以被spring的扫描器扫描到并将该配置类中的bean加入spring容器中.
```

> @ComponentScan

```java
@ComponentScan中有value属性，若不指定value该注解默认会扫描该类所在的包下所有的配置类，
相当于之前的 <context:component-scan>
```

> @EnableAutoConfiguration

```java
EnableAutoConfiguration内部有俩个注解分别为
@AutoConfigurationPackage与@Import({AutoConfigurationImportSelector.class})，
第一个注解的含义就是当SpringBoot应用启动时默认会将启动类所在的package作为自动配置的package，
第二个注解就是根据不同环境导入对应的自动配置类，点进AutoConfigurationImportSelector内部debug你会发现开始springboot准备了一组自动配置类，然后经过springboot内部判断返回一部分自动配置类，而这一部分是Springboot认为是我们所需要的,好处就是让我们免去了手写配置.
```

**SpringBoot 官网描述**

```xml
@EnableAutoConfiguration：启用Spring Boot的自动配置机制
@ComponentScan：启用@Component对应用程序所在的软件包的扫描（请参阅最佳实践）
@Configuration：允许在上下文中注册额外的bean或导入其他配置类
```









### 配置文件

​		SpringBoot使用的全局配置文件，主要就是原本springboot为用户做了许多自动配置，若某些配置用户不满意可以通过全局配置文件修改，比如访问项目的端口号.

<u>**位置                                              **</u>

​	src/main/sources下 或 类路径/config 下

<u>**文件固定名称及类型          **</u>

​	application.properties || application.yml 

<u>**YML                            **</u>

YAML (YAML Aint Markup Language)是一种标记语言，通常以.yml为后缀的文件，是一种直观的能够被电脑识别的数据序列化格式，并且容易被人类阅读，容易和脚本语言交互的，可以被支持YAML库的不同的编程语言程序导入，一种专门用来写配置文件的语言。可用于如： Java，C/C++, Ruby, Python, Perl, C#, PHP等。

<u>**YML语法                         **</u>

