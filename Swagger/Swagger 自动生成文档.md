# Swagger 自动生成文档

​	只需较少的配置即可生成调用接口的详细文档，解决了前后端交互数据可能描述不准确

**pom 依赖**

```xml
<!-- swagger2 核心包 -->
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.6.1</version>
</dependency>
<!-- swagger2 ui包 -->
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.6.1</version>
</dependency>
```

创建 Swagger 的配置类

```java
@Configuration
@EnableSwagger2  // 开启 Swagger
public class SwaggerConfig {
    
    // 配置Swagger的 Docket 实例
    @Bean
    public Docket docket(ApiInfo apiInfo){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo);
    }
    // 重写Swagger的ui的展示信息
    @Bean
    public ApiInfo apiInfo(){
        // 作者信息
        final Contact DEFAULT_CONTACT = new Contact("洛洛塔力.", "", "");
        return new ApiInfo("Demo-Mapper 测试文档",
                "2020年5月20日创建 测试文档",
                "d1.0",
                "",
                DEFAULT_CONTACT,
                "znsd",
                "", new ArrayList());
    }
}
```

> 项目启动Swagger的 UI 地址

```tex
http://localhost:8080/swagger-ui.html
```



